import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../../../services/auth.service';
@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  collapsed = false;
  nome = '';
  perfis: string[] = [];
  menuItems: any[] = [];
  logado = false;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    const data = localStorage.getItem('usuario');
    if (data) {
      this.logado = true;
      const usuario = JSON.parse(data);
      this.nome = usuario.nome;
      this.perfis = usuario.perfis;

      this.configurarMenu(this.perfis); // usando o primeiro perfil
    }
  }

  configurarMenu(perfis: string[]) {
    const baseMenu = [
      { label: 'Dashboard', icon: 'fas fa-home', route: '/dashboard' },
    ];

    const adminMenu = [
      { label: 'Usuários', icon: 'fas fa-users', route: '/users' },
      { label: 'Todos os Chamados', icon: 'fas fa-list', route: '/alltickets' },
    ];

    const tecnicoMenu = [
      {
        label: 'Chamados',
        icon: 'fas fa-wrench',
        route: '/mytickets',
      },
    ];

    const clienteMenu = [
      { label: 'Abrir Chamado', icon: 'fas fa-plus', route: '/ticket' },
      {
        label: 'Meus Chamados',
        icon: 'fas fa-ticket-alt',
        route: '/mytickets',
      },
    ];

    this.menuItems = [...baseMenu];

    const addedRoutes = new Set<string>(); // evita itens duplicados

    if (perfis.includes('ADMIN')) {
      adminMenu.forEach((item) => {
        if (!addedRoutes.has(item.route)) {
          this.menuItems.push(item);
          addedRoutes.add(item.route);
        }
      });
    }

    if (perfis.includes('TECNICO')) {
      tecnicoMenu.forEach((item) => {
        if (!addedRoutes.has(item.route)) {
          this.menuItems.push(item);
          addedRoutes.add(item.route);
        }
      });
    }

    if (perfis.includes('CLIENTE')) {
      clienteMenu.forEach((item) => {
        if (!addedRoutes.has(item.route)) {
          this.menuItems.push(item);
          addedRoutes.add(item.route);
        }
      });
    }

    // Logout (sempre)
    this.menuItems.push({
      label: 'Logout',
      icon: 'fas fa-sign-out-alt',
      route: '#',
      action: () => this.logout(),
    });
  }

  toggleSidebar() {
    this.collapsed = !this.collapsed;
  }

  logout() {
    Swal.fire({
      title: 'Tem certeza que deseja sair?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sim, sair!',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        // 🟢 Pega o refreshToken do localStorage
        const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');
        const refreshToken = usuario.refreshToken;
  
        // 🟡 Faz requisição para logout (revoga o token no backend)
        this.authService.logoutComToken(refreshToken).subscribe({
          next: () => {
            localStorage.removeItem('usuario');
            this.router.navigateByUrl('/login');
          },
          error: () => {
            // mesmo que falhe, limpa o storage e redireciona
            localStorage.removeItem('usuario');
            this.router.navigateByUrl('/login');
          }
        });
      }
    });
  }
  
}
