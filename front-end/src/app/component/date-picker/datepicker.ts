
import { NgbDateParserFormatter,NgbDate, NgbDateStruct, NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Injectable } from '@angular/core';


@Injectable()
export class CustomAdapter extends NgbDateAdapter<Date> {
  constructor(private datepipe: DatePipe){
    super();
  }
	readonly DELIMITER = '-';

	fromModel(value: Date | null): NgbDateStruct | null {
		if (value && value instanceof Date) {
      let day=this.datepipe.transform(value, 'dd')
      let month=this.datepipe.transform(value, 'MM')
      let year=this.datepipe.transform(value, 'yyyy')
      if(day && month && year){
        let a={
          day: parseInt(day, 10),
          month: parseInt(month, 10),
          year: parseInt(year, 10),
        }
      
        return a;
      }
		}
		return null;
	}

	toModel(date: NgbDateStruct | null): Date | null {
    if(date){
      let d=date ? date.year + this.DELIMITER + date.month + this.DELIMITER +  date.day : '';
      return new Date(d);
    }
    return null
	}
}

/**
 * This Service handles how the date is rendered and parsed from keyboard i.e. in the bound input field.
 */
@Injectable()
export class CustomDateParserFormatter extends NgbDateParserFormatter {
	readonly DELIMITER = '/';

	parse(value: string): NgbDateStruct | null {
    
		if (value) {
			const date = value.split(this.DELIMITER);
			return {
				day: parseInt(date[0], 10),
				month: parseInt(date[1], 10),
				year: parseInt(date[2], 10),
			};
		}
		return null;
	}

	format(date: NgbDateStruct | null): string {
		return date ? date.day + this.DELIMITER + date.month + this.DELIMITER + date.year : '';
	}
}