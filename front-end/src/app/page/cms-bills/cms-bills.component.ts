import { Component, Injector } from '@angular/core';
import { Bill, BillSearchRequest } from 'src/app/model/model';
import { BaseComponent } from '../base/BaseComponent';
import { paymentList, statusList } from 'src/app/common/utils';
import { BillService } from 'src/app/services/bill/bill.service';
import { MatDialog } from '@angular/material/dialog';
import { FormControl } from '@angular/forms';
import { FwError } from 'src/app/common/constants';
import { ConfirmDialogComponent } from 'src/app/component/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-cms-bills',
  templateUrl: './cms-bills.component.html',
  styleUrls: ['./cms-bills.component.scss'],
})
export class CmsBillsComponent extends BaseComponent<Bill> {
  statusList = statusList;
  paymentList = paymentList;
  billSearch: BillSearchRequest = new BillSearchRequest();
  constructor(
    private billService: BillService,
    private dialog: MatDialog,
    private injector: Injector
  ) {
    super(injector);
  }
  searchForm = this.formBuilder.group({
    phoneNumber: new FormControl(),
    name: new FormControl(),
    status: new FormControl(),
    payment: new FormControl(),
  });

  ngOnInit(): void {
    this.onInit();
  }

  override search(page: any) {
    this.page.curentPage = page;
    this.billService
      .searchPaging(this.page, this.billSearch)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.totalElements) this.total = res.data.totalElements;
          if (res.data?.content)
            this.asyncData = res.data.content.map((item) => {
              item.status == '1' && (item.status = 'Hiệu lực');
              item.status == '0' && (item.status = 'Không hiệu lực');
              item.payment == 1 && (item.payment = 'Thanh toán khi nhận hàng');
              item.payment == 0 && (item.payment = 'Online');
              return item;
            });
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }
  delete(id: any) {
    this.billService.delete(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        this.toastrs.success('Thành công');
        window.location.reload();
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
  confirmDelete(id: number | undefined, name: String | undefined) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Thông báo',
        name: name,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === true && id !== undefined) {
        this.delete(id);
      }
    });
  }
}
