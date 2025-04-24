import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { endpoints } from '../../../configurations/environments';

@Component({
  selector: 'app-ticket',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './ticket.component.html',
  styleUrl: './ticket.component.css',
})
export class TicketComponent {
  tipos: any[] = [];
  erros: any = null;
  mensagem: string = '';
  arquivosSelecionados: File[] = [];

  constructor(private http: HttpClient, private toastr: ToastrService) {}

  form = new FormGroup({
    titulo: new FormControl('', Validators.required),
    descricao: new FormControl('', Validators.required),
    tipoChamado: new FormControl('', Validators.required),
  });

  ngOnInit() {}

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.arquivosSelecionados = Array.from(input.files);
    }
  }

  onSubmit() {
    const chamadoPayload = {
      titulo: this.form.value.titulo,
      descricao: this.form.value.descricao,
      tipoChamado: this.form.value.tipoChamado,
    };

    // Primeiro: cria o chamado
    this.http
      .post<any>(`${endpoints.criar_chamado}`, chamadoPayload)
      .subscribe({
        next: (res) => {
          const chamadoId = res.id;

          // Segundo: se houver arquivos, envia os anexos
          if (this.arquivosSelecionados.length > 0) {
            const formData = new FormData();
            this.arquivosSelecionados.forEach((arquivo) => {
              formData.append('arquivos', arquivo);
            });

            this.http
              .post(`${endpoints.upload_anexo(chamadoId!)}`, formData)
              .subscribe({
                next: () => {
                  this.toastr.success('Chamado criado com anexos!', '', {
                    progressBar: true,
                    timeOut: 4000,
                    positionClass: 'toast-bottom-right',
                  });
                  this.form.reset();
                  this.arquivosSelecionados = [];
                },
                error: () => {
                  this.toastr.error(
                    'Chamado criado, mas falha ao enviar anexos.',
                    '',
                    {
                      progressBar: true,
                      timeOut: 4000,
                      positionClass: 'toast-bottom-right',
                    }
                  );
                },
              });
          } else {
            // Se não tem arquivos
            this.toastr.success('Chamado criado com sucesso!', '', {
              progressBar: true,
              timeOut: 4000,
              positionClass: 'toast-bottom-right',
            });
            this.form.reset();
          }
        },
        error: () => {
          this.toastr.error('Erro ao criar chamado.', '', {
            progressBar: true,
            timeOut: 4000,
            positionClass: 'toast-bottom-right',
          });
        },
      });
  }
  cleanForm() {
    this.form.reset();
  }
}
