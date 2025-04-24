import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { endpoints } from '../../../configurations/environments';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-ticket-detail',
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule],
  templateUrl: './ticket-detail.component.html',
  styleUrl: './ticket-detail.component.css',
})
export class TicketDetailComponent {
  form: FormGroup;
  formMensagem: FormGroup;
  chamado: any;
  mensagens: any[] = [];
  usuario: any;
  perfil: string = '';
  anexos: any[] = [];

  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);
  private toastr = inject(ToastrService);

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    const data = localStorage.getItem('usuario');

    if (data) {
      this.usuario = JSON.parse(data);
      this.perfil = this.usuario.perfis[0];
    }

    this.http.get(endpoints.obter_chamado(id!)).subscribe((chamado: any) => {
      this.chamado = chamado;

      this.form.patchValue({
        titulo: chamado.titulo,
        descricao: chamado.descricao,
        tipoChamado: chamado.tipoChamado,
        status: chamado.status,
      });

      if (this.ehTecnico) {
        this.form.get('status')?.enable();
      }
      this.carregarMensagens();
      this.carregarAnexos();
    });
  }

  get ehTecnico(): boolean {
    return this.perfil === 'TECNICO' || this.perfil === 'ADMIN';
  }

  constructor() {
    this.form = new FormGroup({
      titulo: new FormControl({ value: '', disabled: true }),
      descricao: new FormControl(''),
      tipoChamado: new FormControl({ value: '', disabled: true }),
      status: new FormControl({ value: '', disabled: true }),
    });

    this.formMensagem = new FormGroup({
      conteudo: new FormControl('', Validators.required),
    });
  }

  atualizarChamado(): void {
    const payload = {
      ...this.form.getRawValue(),
      id: this.chamado.id,
    };

    this.http
      .put(endpoints.atualizar_chamado(payload.id!), payload)
      .subscribe(() => {
        this.toastr.success('Chamado atualizado com sucesso!', '', {
          progressBar: true,
          timeOut: 4000,
          positionClass: 'toast-top-right',
        });
      });
  }

  carregarMensagens(): void {
    this.http
      .get(endpoints.listar_mensagem(this.chamado.id))
      .subscribe((res: any) => {
        this.mensagens = res;
      });
  }

  enviarMensagem(): void {
    if (this.formMensagem.invalid) return;

    const conteudo = this.formMensagem.value.conteudo;

    this.http
      .post(endpoints.enviar_mensagem(this.chamado.id), { conteudo })
      .subscribe((novaMensagem: any) => {
        this.mensagens.push(novaMensagem);
        this.formMensagem.reset();
      });
  }

  carregarAnexos(): void {
    this.http.get<any[]>(endpoints.listar_anexos(this.chamado.id)).subscribe({
      next: (res) => {
        this.anexos = res;
      },
      error: () => {
        this.toastr.warning('Não foi possível carregar os anexos.');
      },
    });
  }

  baixarAnexo(anexo: any): void {
    this.http
      .get(endpoints.download_anexo(anexo.id), {
        responseType: 'blob',
        observe: 'response',
      })
      .subscribe({
        next: (res) => {
          const blob = new Blob([res.body!], {
            type: res.headers.get('Content-Type')!,
          });
          const url = window.URL.createObjectURL(blob);

          const a = document.createElement('a');
          a.href = url;
          a.download = anexo.nome;
          a.click();

          window.URL.revokeObjectURL(url);
        },
        error: (err) => {
          console.error('Erro ao baixar anexo:', err);
        },
      });
  }
}
