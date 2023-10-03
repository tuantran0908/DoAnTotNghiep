import { Component, Injector } from '@angular/core';
import { Bill } from 'src/app/model/model';
import { BaseComponent } from '../../base/BaseComponent';
import { BillService } from 'src/app/services/bill/bill.service';
import { FormControl, Validators } from '@angular/forms';
import { FwError } from 'src/app/common/constants';
import { paymentList } from 'src/app/common/utils';

@Component({
  selector: 'app-create-update-cms-bills',
  templateUrl: './create-update-cms-bills.component.html',
  styleUrls: ['./create-update-cms-bills.component.scss'],
})
export class CreateUpdateCmsBillsComponent extends BaseComponent<Bill> {
  billDetail: Bill = new Bill();
  paymentList = paymentList;

  constructor(private billService: BillService, private injector: Injector) {
    super(injector);
  }
  billStatus: boolean = false;
  detailForm = this.formBuilder.group({
    id: new FormControl([Validators.required]),
    name: new FormControl([Validators.required]),
    status: new FormControl('', [Validators.required]),
    phoneNumber: new FormControl('', [Validators.required]),
    payment: new FormControl('', [Validators.required]),
    address: new FormControl('', [Validators.required]),
  });

  ngOnInit(): void {
    this.onInit();
  }
  override getDetail(id: any) {
    this.billService.getDetail(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.billDetail = res.data;
          this.billStatus = this.billDetail.status == '1';
          this.billDetail.payment = this.billDetail.payment == 1 ? '1' : '0';
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
  override update() {
    this.isSubmit = true;
    if (this.detailForm.status == 'INVALID') {
      this.focusInValidForm();
      return;
    } else if (this.detailForm.status == 'PENDING') {
      this.toastrs.warning('Hệ thống đang xử lý! Vui lòng đợi');
      return;
    } else {
      if (this.billStatus) {
        this.billDetail.status = '1';
      } else {
        this.billDetail.status = '0';
      }
      this.billService.update(this.billDetail).subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data) {
            this.billDetail = res.data;
          }
          this._location.back();
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
    }
  }
  override create() {
    this.isSubmit = true;
    if (this.detailForm.status == 'INVALID') {
      this.focusInValidForm();
      return;
    } else if (this.detailForm.status == 'PENDING') {
      this.toastrs.warning('Hệ thống đang xử lý! Vui lòng đợi');
      return;
    } else {
      if (this.billStatus) {
        this.billDetail.status = '1';
      } else {
        this.billDetail.status = '0';
      }
      this.billService.create(this.billDetail).subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data) {
            this.billDetail = res.data;
          }
          this._location.back();
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
    }
  }
}
