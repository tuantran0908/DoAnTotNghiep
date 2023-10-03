import { Component, InjectionToken, Injector } from '@angular/core';
import { Bill, BillDetail, Review, User } from 'src/app/model/model';
import { BaseComponent } from '../base/BaseComponent';
import { BillService } from 'src/app/services/bill/bill.service';
import { FwError } from 'src/app/common/constants';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/component/confirm-dialog/confirm-dialog.component';
import { ReviewDialogComponent } from 'src/app/component/review-dialog/review-dialog.component';
import { ScrollStrategy } from '@angular/cdk/overlay';
import { ReviewService } from 'src/app/services/review/review.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss'],
})
export class PaymentComponent extends BaseComponent<Bill> {
  billDetail: Bill = new Bill();
  transport: number = 30000;
  payment: string = 'Thanh toán tiền mặt khi nhận hàng';
  constructor(
    private billService: BillService,
    private authService: AuthService,
    private reviewService: ReviewService,
    private dialog: MatDialog,
    private router: Router,
    private injector: Injector
  ) {
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
        }
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
          user.id = this.authService.getAuthorization().id;
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
  onPay() {
    this.billDetail.status = '2';
    this.billDetail.total = this.billDetail.total! + 30000;
    console.log(this.billDetail);

    this.billService.update(this.billDetail).subscribe((res) => {
      this.toastrs.success('Thanh toán thành công');
      this.router.navigate(['/bills', this.billDetail.id]);
    });
  }
}
