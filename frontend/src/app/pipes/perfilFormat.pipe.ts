import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'perfilFormat',
})
export class PerfilFormatPipe implements PipeTransform {
  transform(value: string[] | null | undefined): string {
    if (!value || value.length === 0) return 'Nenhum perfil';
    return value.join(' / ');
  }
}
