import { Component, inject } from '@angular/core';
import { endpoints } from '../../../configurations/environments';
import { HttpClient } from '@angular/common/http';
import {
  ChartConfiguration,
  ChartData,
  ChartOptions,
  ChartType,
} from 'chart.js';
import { CommonModule } from '@angular/common';
import { NgChartsModule } from 'ng2-charts';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, NgChartsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {
  constructor(private http: HttpClient) {}

  // Contadores dos cards
  totalChamados = 0;
  chamadosAbertos = 0;
  chamadosFinalizados = 0;
  chamadosEmAndamento = 0;
  nome = '';
  perfis: string[] = [];
  logado = false;
  idUser: any = '';
  endpointEstatistica: any = '';
  endpointChamadosRecentes: any = '';

  // Dados dos chamados recentes
  chamadosRecentes: any[] = [];

  // Gráfico de pizza
  public pieChartLabels: string[] = [];
  public pieChartData: ChartData<'pie', number[], string | string[]> = {
    labels: [],
    datasets: [
      {
        data: [],
        backgroundColor: ['#f87171', '#facc15', '#4ade80'],
      },
    ],
  };

  public pieChartOptions: ChartOptions<'pie'> = {
    responsive: true,
    plugins: {
      legend: {
        position: 'bottom',
      },
    },
  };

  public pieChartType: 'pie' = 'pie';

  ngOnInit(): void {
    const data = localStorage.getItem('usuario');
    if (data) {
      this.logado = true;
      const usuario = JSON.parse(data);
      this.nome = usuario.nome;
      this.perfis = usuario.perfis;
      this.idUser = usuario.id;
    }

    this.verificarEndpoints();
    this.carregarEstatisticas();
    this.carregarChamadosRecentes();
  }

  carregarEstatisticas() {
    this.http.get<any[]>(`${this.endpointEstatistica}`).subscribe((data) => {
      const labels = data.map((d) => this.formatarStatus(d.status));
      const valores = data.map((d) => d.qtdChamados);

      this.pieChartLabels = labels;
      this.pieChartData = {
        labels: labels,
        datasets: [
          {
            data: valores,
            backgroundColor: ['#f87171', '#facc15', '#4ade80'],
          },
        ],
      };

      this.totalChamados = valores.reduce((acc, qtd) => acc + qtd, 0);
      this.chamadosAbertos = this.extrairQuantidadePorStatus(data, 'ABERTO');
      this.chamadosEmAndamento = this.extrairQuantidadePorStatus(
        data,
        'EM_ANDAMENTO'
      );
      this.chamadosFinalizados = this.extrairQuantidadePorStatus(
        data,
        'FINALIZADO'
      );
    });
  }

  verificarEndpoints() {
    if (this.perfis.includes('ADMIN')) {
      this.endpointEstatistica = endpoints.dashboard;
      this.endpointChamadosRecentes = endpoints.consultar;
    } else if (this.perfis.includes('TECNICO')) {
      this.endpointEstatistica = endpoints.dashboard;
      this.endpointChamadosRecentes = endpoints.listar_por_perfil;
    } else {
      this.endpointEstatistica = endpoints.dashboard_usuario(this.idUser!);
      this.endpointChamadosRecentes = endpoints.listar_por_perfil;
    }
  }

  carregarChamadosRecentes() {
    this.http
      .get<any[]>(`${this.endpointChamadosRecentes}`)
      .subscribe((data) => {
        this.chamadosRecentes = data.slice(0, 10);
      });
  }

  extrairQuantidadePorStatus(data: any[], status: string): number {
    return data.find((d) => d.status === status)?.qtdChamados || 0;
  }

  formatarStatus(status: string): string {
    return status
      .toLowerCase()
      .replace(/_/g, ' ') // troca underscores por espaços
      .replace(/\b\w/g, (letra) => letra.toUpperCase()); // capitaliza cada palavra
  }
}
