import { Component, Injector } from '@angular/core';
import { BaseComponent } from '../base/BaseComponent';
import {
  Bill,
  BillDetail,
  BillSearchRequest,
  LoginResponse,
  Review,
  User,
} from 'src/app/model/model';
import { AuthService } from 'src/app/services/auth/auth.service';
import { FwError } from 'src/app/common/constants';
import { FormControl, Validators } from '@angular/forms';
import { paymentList } from 'src/app/common/utils';
import { SelectionModel } from '@angular/cdk/collections';
import { ConfirmDialogComponent } from 'src/app/component/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { BillService } from 'src/app/services/bill/bill.service';
import { ReviewService } from 'src/app/services/review/review.service';
import { ReviewDialogComponent } from 'src/app/component/review-dialog/review-dialog.component';

@Component({
  selector: 'app-bills',
  templateUrl: './bills.component.html',
  styleUrls: ['./bills.component.scss'],
})
export class BillsComponent extends BaseComponent<Bill> {
  billSearch: BillSearchRequest = new BillSearchRequest();
  user?: LoginResponse;
  constructor(
    private authService: AuthService,
    private billService: BillService,
    private reviewService: ReviewService,
    private dialog: MatDialog,
    private injector: Injector
  ) {
    super(injector);
  }
  ngOnInit(): void {
    this.user = this.authService.getAuthorization();
    this.onInit();
  }
  override search(page: any) {
    this.page.curentPage = page;
    this.billSearch.userId = this.user!.id;
    this.billService
      .searchPaging(this.page, this.billSearch)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.content) this.asyncData = res.data?.content;
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }
  reviewDialog(billDetails: BillDetail[] | undefined) {
    let billDetailsNew: BillDetail[] = [];
    billDetails?.forEach((item) => {
      let itemNew = new BillDetail();
      itemNew.bill = item.bill;
      itemNew.quantity = item.quantity;
      itemNew.giaoTrinh = item.giaoTrinh;
      billDetailsNew.push(itemNew);
    });
    const dialogRef = this.dialog.open(ReviewDialogComponent, {
      data: {
        title: 'Đánh Giá Sản Phẩm',
        billDetails: billDetailsNew,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result === true) {
        billDetailsNew?.forEach((element: any) => {
          let review = new Review();
          review.id = 1;
          review.star = element.star;
          review.message = element.message;
          review.giaoTrinh = element.giaoTrinh;
          let user = new User();
          user.id = this.user?.id;
          review.user = user;
          this.reviewService.create(review).subscribe((res) => {
            if (FwError.THANHCONG == res.errorCode) {
              if (res.data) {
                this.toastrs.success('Nhận xét thành công');
              }
            } else {
              this.toastrs.error(res.errorMessage);
            }
          });
        });
      }
    });
  }
}
