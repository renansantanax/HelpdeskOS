import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { osservice } from '../configurations/environments';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient, public router: Router) {}

  get accessToken() {
    return this.getUsuario()?.token;
  }

  get refreshToken() {
    return this.getUsuario()?.refreshToken;
  }

  getUsuario() {
    const data = localStorage.getItem('usuario');
    return data ? JSON.parse(data) : null;
  }

  setUsuario(data: any) {
    localStorage.setItem('usuario', JSON.stringify(data));
  }

  clear() {
    localStorage.removeItem('usuario');
  }

  refreshTokenRequest() {
    return this.http.post<any>(`${osservice}/auth/refresh`, {
      refreshToken: this.refreshToken,
    });
  }

  logout() {
    return this.http
      .post(`${osservice}/auth/logout`, {
        refreshToken: this.refreshToken,
      })
      .subscribe(() => {
        this.clear();
        this.router.navigate(['/login']);
      });
  }

  logoutComToken(refreshToken: string) {
    return this.http.post(`${osservice}/auth/logout`, {
      refreshToken: refreshToken,
    });
  }
}
