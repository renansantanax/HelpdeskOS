import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { endpoints } from '../../../configurations/environments';
import { RouterModule } from '@angular/router';
import { User } from '../../../models/user.model';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { animate, style, transition, trigger } from '@angular/animations';
import Swal from 'sweetalert2';
import { PerfilFormatPipe } from '../../../pipes/perfilFormat.pipe';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-users',
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    FormsModule,
    PerfilFormatPipe,
  ],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('200ms ease-in', style({ opacity: 1 })),
      ]),
    ]),
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css',
})
export class UsersComponent implements OnInit {
  user: User[] = [];
  modalAberto = false;
  usuarioSelecionado!: User;
  mensagem: string = '';
  error: any = '';
  constructor(private http: HttpClient, private toastr: ToastrService) {}

  form = new FormGroup({
    nome: new FormControl('', Validators.required),
    email: new FormControl('', Validators.required),
    perfis: new FormControl<string[]>([]),
  });

  togglePerfil(event: any) {
    const perfis = this.form.get('perfis')?.value || [];
    if (event.target.checked) {
      perfis.push(event.target.value);
    } else {
      const index = perfis.indexOf(event.target.value);
      if (index > -1) perfis.splice(index, 1);
    }
    this.form.get('perfis')?.setValue(perfis);
  }

  abrirModal(usuario: User) {
    this.usuarioSelecionado = { ...usuario };
    this.modalAberto = true;
    this.form.patchValue({
      nome: usuario.nome,
      email: usuario.email,
      perfis: [...usuario.perfis],
    });
  }

  fecharModal() {
    this.modalAberto = false;
  }

  salvarEdicao() {
    setTimeout(() => {
      this.fecharModal();
    }, 300);
    this.http
      .put(
        `${endpoints.atualizar_usuario(this.usuarioSelecionado.id)}`,
        this.form.value,
        { responseType: 'text' }
      )
      .subscribe({
        next: (data) => {
          this.toastr.success('Usuário atualizado com sucesso!', '', {
            progressBar: true,
            timeOut: 4000,
            positionClass: 'toast-top-right',
          });
          this.ngOnInit();
        },
        error: (e) => {
          this.error = e;
        },
      });
  }

  togglerPerfil(event: Event, perfil: string) {
    const checked = (event.target as HTMLInputElement).checked;
    const perfis = this.form.get('perfis')?.value || [];

    const novosPerfis = checked
      ? [...perfis, perfil]
      : perfis.filter((p: string) => p !== perfil);

    this.form.get('perfis')?.setValue(novosPerfis);
  }

  usuarios: any[] = [];
  erros: any[] = [];

  ngOnInit(): void {
    this.http.get(`${endpoints.listar_usuarios}`).subscribe({
      next: (data) => {
        this.usuarios = data as any[];
      },
      error: (e) => {
        this.erros = e;
      },
    });
  }

  onDelete(id: string) {
    Swal.fire({
      title: 'Deseja excluir esse usuário?',
      text: 'Você não poderá reverter essa ação!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sim, excluir!',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.http
          .delete(`${endpoints.excluir_usuario(id!)}`, { responseType: 'text' })
          .subscribe({
            next: (data) => {
              this.toastr.success('Usuário excluído com sucesso!', '', {
                progressBar: true,
                timeOut: 4000,
                positionClass: 'toast-top-right',
              });
              this.ngOnInit();
            },
            error: (e) => {
              this.toastr.error('Erro ao excluír usuário!', '', {
                progressBar: true,
                timeOut: 4000,
                positionClass: 'toast-top-right',
              });
            },
          });
      }
    });
  }
}
