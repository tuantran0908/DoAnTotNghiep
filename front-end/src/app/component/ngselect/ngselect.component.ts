import {
  Component,
  EventEmitter,
  forwardRef,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  Type,
} from '@angular/core';
import {
  ControlValueAccessor,
  NgModel,
  NG_VALUE_ACCESSOR,
} from '@angular/forms';
import {
  catchError,
  concat,
  noop,
  Observable,
  of,
  Subject,
  switchMap,
  tap,
} from 'rxjs';
import { NgParam } from 'src/app/model/model';
import { BaseService } from 'src/app/services/BaseService';

@Component({
  selector: 'app-ngselect',
  templateUrl: './ngselect.component.html',
  styleUrls: ['./ngselect.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => NgselectComponent),
      multi: true,
    },
  ],
})
export class NgselectComponent implements ControlValueAccessor, OnChanges {
  @Input() service?: any;
  @Input() name: any;
  @Input() readonly: any;
  @Input() multiple:boolean=true;
  @Input()
  customparam: NgParam[] = [];

  constructor() {}

  items$: Observable<any> = of([]);
  loading = false;
  input$ = new Subject<string>();
  trackByFn(item: any) {
    return item.track;
  }

  loadData() {
    // this.service.searchSlect2(null).subscribe((res: any)=>{
    this.items$ = concat(
      this.service.searchSlect2(null, this.customparam), // default items
      this.input$.pipe(
        tap(() => (this.loading = true)),
        switchMap((term) =>
          this.service
            .searchSlect2(term, this.customparam)
            .then((_item: any) => {
              this.loading = false;
              return _item;
            })
        )
      )
    );
    // })
  }

  ngOnInit(): void {
    if (this.service) {
      this.loadData();
    }
  }

  //The internal data model
  private innerValue: any = '';

  //Placeholders for the callbacks which are later providesd
  //by the Control Value Accessor
  private onTouchedCallback: () => void = noop;
  private onChangeCallback: (_: any) => void = noop;

  //get accessor
  get value(): any {
    return this.innerValue;
  }

  //set accessor including call the onchange callback
  set value(v: any) {
    if (v !== this.innerValue) {
      this.innerValue = v;
      this.onChangeCallback(v);
    }
  }

  //Set touched on blur
  onBlur() {
    this.onTouchedCallback();
  }

  //From ControlValueAccessor interface
  writeValue(value: any) {
    if (value !== this.innerValue) {
      this.innerValue = value;
    }
  }

  //From ControlValueAccessor interface
  registerOnChange(fn: any) {
    this.onChangeCallback = fn;
  }

  //From ControlValueAccessor interface
  registerOnTouched(fn: any) {
    this.onTouchedCallback = fn;
  }

  ngOnChanges(): void {
    this.ngOnInit();
  }
}
