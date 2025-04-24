import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'idMask' })
export class IdMaskPipe implements PipeTransform {
  transform(value: number): string {
    return `CH-${value.toString().padStart(3, '0')}`;
  }
}
