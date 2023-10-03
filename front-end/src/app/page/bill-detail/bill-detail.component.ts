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
import { FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-bill-detail',
  templateUrl: './bill-detail.component.html',
  styleUrls: ['./bill-detail.component.scss'],
})
export class BillDetailComponent extends BaseComponent<Bill> {
  billDetail: Bill = new Bill();
  reason: string | undefined;
  message: string | undefined;
  reasonList: any = [
    { label: 'Đổi hình thức thanh toán', value: 1 },
    { label: 'Đặt trùng', value: 2 },
    { label: 'Thêm/bớt sản phẩm', value: 3 },
    { label: 'Thay đổi địa chỉ giao hàng', value: 4 },
    { label: 'Không còn nhu cầu', value: 5 },
  ];
  constructor(
    private billService: BillService,
    private authService: AuthService,
    private reviewService: ReviewService,
    private dialog: MatDialog,
    private injector: Injector
  ) {
    super(injector);
  }
  cancelForm = this.formBuilder.group({
    reason: new FormControl('', [Validators.required]),
    message: new FormControl('', [Validators.required]),
  });
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
          if (element.star && element.message) {
            let review = new Review();
            review.id = 1;
            review.star = element.star;
            review.message = element.message;
            review.giaoTrinh = element.giaoTrinh;
            let user = new User();
            user.id = this.authService.getAuthorization().id;
            review.user = user;
            this.reviewService.create(review).subscribe((res) => {
              this.toastrs.error(res.errorMessage);
            });
          }
        });
        this.toastrs.success('Nhận xét thành công');
      }
    });
  }
  onCancelBill() {
    this.isSubmit = true;
    if (this.cancelForm.status == 'INVALID') {
      this.focusInValidForm();
      return;
    } else if (this.cancelForm.status == 'PENDING') {
      this.toastrs.warning('Hệ thống đang xử lý! Vui lòng đợi');
      return;
    } else {
      this.billDetail.status = '0';
      this.billService.update(this.billDetail).subscribe((res) => {
        this.toastrs.success('Hủy đơn hàng thành công');
      });
    }
  }
}
