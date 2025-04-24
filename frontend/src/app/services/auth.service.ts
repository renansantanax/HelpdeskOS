import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { osservice } from '../configurations/environments';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient, public router: Router) {}

  get accessToken(): string | null {
    return this.getUsuario()?.token ?? null;
  }

  getUsuario(): any {
    const data = localStorage.getItem('usuario');
    return data ? JSON.parse(data) : null;
  }

  setUsuario(data: any): void {
    localStorage.setItem('usuario', JSON.stringify(data));
  }

  clear(): void {
    localStorage.removeItem('usuario');
  }

  logout(): void {
    this.clear();
    this.router.navigate(['/login']);
  }
}
