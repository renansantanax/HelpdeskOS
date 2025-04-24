package com.renan.osservice.infrastructure.components;

import java.util.Date;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renan.osservice.domain.dtos.notificacao.NotificacaoChamadoDto;

@Component
public class RabbitMQConsumerComponent {
	@Autowired MailSenderComponent mailSenderComponent;
	@Autowired ObjectMapper objectMapper;
	
	/*
	 * Método responsável por ler cada registro / mensagem
	 * contida na fila do RabbitMQ
	 */
	@RabbitListener(queues = "os_service")
	public void receiveMessage(@Payload String message) {
		
		try {
			
			//deserializar os dados da conta gravados em JSON na fila
			var chamado = objectMapper.readValue(message, NotificacaoChamadoDto.class);
			
			var to = "destinatario@email.com";
			var subject = "Novo chamado! " + new Date();
			
			var body = String.format("""
           	    <html>
           	    <body style="font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4;">
           	        <div style="max-width: 600px; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px #ccc;">
           	            <h2 style="color: #4CAF50;">Chamado aberto com sucesso!</h2>
           	            <p>Olá,</p>
           	            <p>O chamado abaixo foi aberto no sistema:</p>
           	            <table style="width: 100%%; border-collapse: collapse;">
           	                <tr>
           	                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Titulo:</strong></td>
           	                    <td style="padding: 10px; border-bottom: 1px solid #ddd;">%s</td>
           	                </tr>
           	                <tr>
           	                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Descrição:</strong></td>
           	                    <td style="padding: 10px; border-bottom: 1px solid #ddd;">%s</td>
           	                </tr>
           	                <tr>
           	                    <td style="padding: 10px; border-bottom: 1px solid #ddd;"><strong>Tipo:</strong></td>
           	                    <td style="padding: 10px; border-bottom: 1px solid #ddd;">%s</td>
           	                </tr>
           	            </table>
           	            <p>Entre no sistema para gerenciar o chamado.</p>
           	            <p>Atenciosamente,<br><strong>Equipe TasksOn</strong></p>
           	        </div>
           	    </body>
           	    </html>
           	    """, chamado.getTitulo(), chamado.getDescricao(), chamado.getTipo());
			//enviando o email
			mailSenderComponent.sendMail(to, subject, body);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
