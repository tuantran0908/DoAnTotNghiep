import { Component, Injector } from '@angular/core';
import { Bill } from 'src/app/model/model';
import { BaseComponent } from '../../base/BaseComponent';
import { BillService } from 'src/app/services/bill/bill.service';
import { FwError } from 'src/app/common/constants';

@Component({
  selector: 'app-detail-cms-bills',
  templateUrl: './detail-cms-bills.component.html',
  styleUrls: ['./detail-cms-bills.component.scss'],
})
export class DetailCmsBillsComponent extends BaseComponent<Bill> {
  billDetail: Bill = new Bill();
  constructor(private billService: BillService, private injector: Injector) {
    super(injector);
  }
  ngOnInit(): void {
    this.onInit();
  }
  override getDetail(id: any) {
    this.billService.getDetail(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.billDetail = res.data;
          this.billDetail.status == '1' &&
            (this.billDetail.status = 'Hiệu lực');
          this.billDetail.status == '0' &&
            (this.billDetail.status = 'Không hiệu lực');
          this.billDetail.payment == 1 &&
            (this.billDetail.payment = 'Thanh toán khi nhận hàng');
          this.billDetail.payment == 0 && (this.billDetail.payment = 'Online');
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
}
