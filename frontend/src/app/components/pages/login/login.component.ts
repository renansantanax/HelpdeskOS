import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { endpoints } from '../../../configurations/environments';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, ReactiveFormsModule, FontAwesomeModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  mensagem: string = '';
  erros: any = null;

  //CONSTRUCTOR INIT FOR HTTP CLIENT
  constructor(
    private http: HttpClient,
    private router: Router,
    private toastr: ToastrService,
    private authService: AuthService
  ) {}

  form = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    senha: new FormControl('', [Validators.required, Validators.minLength(8)]),
  });

  //FUNÇÃO PARA CAPTURAR O EVENTO SUBMIT DO FORMULÁRIO
  onSubmit() {
    this.mensagem = '';
    this.erros = null;
    this.http.post(endpoints.auth_usuario, this.form.value).subscribe({
      next: (data: any) => {
        this.authService.setUsuario(data);
        this.toastr.success('Usuário logado com sucesso!', '', {
          progressBar: true,
          timeOut: 4000,
          positionClass: 'toast-top-right',
        });
        this.router.navigateByUrl('/dashboard');
      },
      error: (e: any) => {
        this.mensagem = typeof e.error === 'string' ? e.error : '';
        this.erros = typeof e.error !== 'string' ? e.error : null;
      },
    });
  }

  mostrarSenha: boolean = false;

  alternarSenha() {
    this.mostrarSenha = !this.mostrarSenha;
  }

}
