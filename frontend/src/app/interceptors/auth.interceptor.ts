import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { osservice } from '../configurations/environments';
import { catchError, throwError } from 'rxjs';

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);

  const usuario = auth.getUsuario();

  if (req.url.includes(osservice) && usuario?.token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${usuario.token}`,
      },
    });

    return next(authReq).pipe(
      catchError((err) => {
        // Se o token expirou ou é inválido, desloga o usuário
        if (err.status === 401) {
          auth.clear(); // limpa o localStorage ou sessionStorage
          auth.router.navigate(['/login']); // redireciona pro login
        }

        return throwError(() => err);
      })
    );
  }

  return next(req);
};
