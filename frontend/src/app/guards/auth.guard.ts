import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const usuarioStr = localStorage.getItem('usuario');
    if (!usuarioStr) {
      this.router.navigate(['/login']);
      return false;
    }

    const usuario = JSON.parse(usuarioStr);
    const perfis: string[] = usuario?.perfis || [];

    // Verifica os perfis permitidos definidos na rota
    const perfisPermitidos = route.data['perfis'] as string[];
    if (perfisPermitidos && !perfis.some((perfil) => perfisPermitidos.includes(perfil))) {
      this.router.navigate(['/acesso-negado']);
      return false;
    }

    return true;
  }
}
