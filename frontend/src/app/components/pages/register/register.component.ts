import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { RouterModule } from '@angular/router';
import { endpoints } from '../../../configurations/environments';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  mensagemSucesso: string = '';
  mensagemErro: string = '';
  erros: any = null;

  constructor(private http: HttpClient, private toastr: ToastrService) {}

  form = new FormGroup({
    nome: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    senha: new FormControl('', [Validators.required, Validators.minLength(6)]),
    confirmarSenha: new FormControl('', Validators.required),
    perfis: new FormControl<string[]>([]),
    // recebe array de strings
  });

  onPerfilChange(event: any) {
    const perfis = this.form.get('perfis')?.value || [];
    if (event.target.checked) {
      perfis.push(event.target.value);
    } else {
      const index = perfis.indexOf(event.target.value);
      if (index > -1) perfis.splice(index, 1);
    }
    this.form.get('perfis')?.setValue(perfis);
  }

  onSubmit() {
    this.mensagemSucesso = '';
    this.mensagemErro = '';
    this.erros = null;

    if (this.form.value.senha == this.form.value.confirmarSenha) {
      this.http.post(`${endpoints.criar_usuario}`, this.form.value).subscribe({
        next: (data) => {
          this.toastr.success('Usuário cadastrado com sucesso!', '', {
            progressBar: true,
            timeOut: 4000,
            positionClass: 'toast-top-right',
          });
          this.form.reset();
        },
        error: (e) => {
          try {
            this.toastr.warning('Preencha todos os campos!', '', {
              progressBar: true,
              timeOut: 4000,
              positionClass: 'toast-top-right',
            });
            this.mensagemErro = e.error;
          } catch (error) {
            this.erros = e.error;
          }
        },
      });
    }
  }
}
