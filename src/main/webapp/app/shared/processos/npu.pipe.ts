import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'npu'
})
export class NpuPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (!value) {
      return '';
    }

    const digits = value.toString().replace(/\D/g, '');

    const part1 = digits.substring(0, 7);   
    const part2 = digits.substring(7, 9);    
    const part3 = digits.substring(9, 13);   
    const part4 = digits.substring(13, 14);   
    const part5 = digits.substring(14, 16);   
    const part6 = digits.substring(16, 20);   

    let formatted = part1;
    if (part2) {
      formatted += '-' + part2;
    }
    if (part3) {
      formatted += '.' + part3;
    }
    if (part4) {
      formatted += '.' + part4;
    }
    if (part5) {
      formatted += '.' + part5;
    }
    if (part6) {
      formatted += '.' + part6;
    }

    return formatted;
  }
}
