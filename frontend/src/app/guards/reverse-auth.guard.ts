import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router
} from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class ReverseAuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const usuario = this.authService.getUsuario();

    if (usuario && usuario.token) {
      // Se já estiver logado, redireciona para a home
      this.router.navigate(['/']);
      return false;
    }

    // Se não estiver logado, permite acesso
    return true;
  }
}
