// Base URL
export const osservice = 'http://localhost:8080';

// Endpoints
export const endpoints = {
  criar_chamado: `${osservice}/api/chamados/criar`,
  atualizar_chamado: (id: number | string | number) =>
    `${osservice}/api/chamados/atualizar/${id}`,
  listar_por_perfil: `${osservice}/api/chamados/dashboard/listar-por-perfil`,
  obter_chamado: (id: number | string | number) =>
    `${osservice}/api/chamados/obter/${id}`,
  encerrar_chamado: (id: number | string | number) =>
    `${osservice}/api/chamados/encerrar/${id}`,
  listar_mensagem: (id: number | string | number) =>
    `${osservice}/api/chamados/mensagens/${id}/listar`,
  enviar_mensagem: (id: number | string | number) =>
    `${osservice}/api/chamados/mensagens/${id}/enviar`,
  consultar: `${osservice}/api/chamados/consultar`,
  dashboard: `${osservice}/api/chamados/dashboard/dashboard-geral`,
  dashboard_usuario: (id: number | string | number) =>
    `${osservice}/api/chamados/dashboard/dashboard-usuario/${id}`,

  criar_usuario: `${osservice}/api/usuario/criar`,
  auth_usuario: `${osservice}/api/usuario/autenticar`,
  atualizar_usuario: (id: number | string | number) =>
    `${osservice}/api/usuario/atualizar/${id}`,
  excluir_usuario: (id: number | string | number) =>
    `${osservice}/api/usuario/excluir/${id}`,
  listar_usuarios: `${osservice}/api/usuario/listar`,

  upload_anexo: (id: number | string | number) =>
    `${osservice}/api/chamados/anexo/upload-anexos/${id}`,
  listar_anexos: (id: number | string | number) =>
    `${osservice}/api/chamados/anexo/${id}/anexos`,
  download_anexo: (id: number | string | number) =>
    `${osservice}/api/chamados/anexo/download/${id}`,
};
