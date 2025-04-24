package com.renan.osservice.infrastructure.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailSenderComponent {

	@Autowired JavaMailSender mailSender;
	
	public void sendMail(String to, String subject, String body) throws Exception {
		
		var message = mailSender.createMimeMessage();
		
		var helper = new MimeMessageHelper(message, true);
		
		helper.setTo(to); 
		helper.setSubject(subject); 
		helper.setText(body, true); 
		
		mailSender.send(message);
		
		
	}
	
}