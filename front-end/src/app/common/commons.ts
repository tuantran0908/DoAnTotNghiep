import { DatePipe } from '@angular/common';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class commons {
  constructor(private datepipe: DatePipe) {}

  toDateUri(date: Date) {
    return this.datepipe.transform(date, 'MM/dd/yyyy');
  }
}
