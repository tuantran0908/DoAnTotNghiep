import { Component, Injector } from '@angular/core';
import { BaseComponent } from '../base/BaseComponent';
import {
  Bill,
  BillDetail,
  CartDetail,
  CartDetailSearchRequest,
  LoginResponse,
  User,
} from 'src/app/model/model';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CartDetailService } from 'src/app/services/cart-detail/cart-detail.service';
import { FwError } from 'src/app/common/constants';
import { FormControl, Validators } from '@angular/forms';
import { paymentList } from 'src/app/common/utils';
import { SelectionModel } from '@angular/cdk/collections';
import { ConfirmDialogComponent } from 'src/app/component/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { BillService } from 'src/app/services/bill/bill.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart-detail',
  templateUrl: './cart-detail.component.html',
  styleUrls: ['./cart-detail.component.scss'],
})
export class CartDetailComponent extends BaseComponent<CartDetail> {
  cartDetailSearch: CartDetailSearchRequest = new CartDetailSearchRequest();
  bill: Bill = new Bill();
  user?: LoginResponse;
  paymentList = paymentList;
  selection = new SelectionModel<CartDetail>(true, []);
  oldTotalPrice: number = 0;
  finalTotalPrice: number = 0;
  salePrice: number = 0;
  constructor(
    private authService: AuthService,
    private cartDetailService: CartDetailService,
    private billService: BillService,
    private dialog: MatDialog,
    private router: Router,
    private injector: Injector
  ) {
    super(injector);
  }
  billForm = this.formBuilder.group({
    address: new FormControl('', [Validators.required]),
    name: new FormControl('', [Validators.required]),
    phoneNumber: new FormControl('', [Validators.required]),
  });
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.asyncData!.length;
    return numSelected === numRows;
  }

  headerToggle() {
    if (this.isAllSelected()) {
      this.selection.clear();
      this.setEmptyPrice();
    } else {
      this.asyncData!.forEach((row) => this.selection.select(row));
      this.countTotalCart();
    }
  }
  itemToggle(item: CartDetail) {
    this.selection.toggle(item);
    this.countTotalCart();
  }
  setEmptyPrice() {
    this.oldTotalPrice = 0;
    this.finalTotalPrice = 0;
    this.salePrice = 0;
  }
  countTotalCart() {
    this.setEmptyPrice();
    this.selection.selected.forEach((cart) => {
      if (cart.giaoTrinh?.price && cart.quantity) {
        this.oldTotalPrice += cart.giaoTrinh.price * cart.quantity;
        if (cart.giaoTrinh?.sales) {
          this.finalTotalPrice +=
            ((cart.giaoTrinh.price * (100 - cart.giaoTrinh.sales)) / 100) *
            cart.quantity;
        } else {
          this.finalTotalPrice += cart.giaoTrinh.price * cart.quantity;
        }
      }
    });
    this.salePrice = this.oldTotalPrice - this.finalTotalPrice;
  }
  onClickAdd(cartItem: CartDetail) {
    if (cartItem.quantity! < cartItem.giaoTrinh!.quantityRemaining!) {
      cartItem.quantity! += 1;
      this.updateQuantity(cartItem);
    } else {
      this.toastrs.error('Vượt quá số lượng có sẵn trong kho !!');
    }
  }
  onClickSub(cartItem: CartDetail) {
    if (cartItem.quantity! > 1) {
      cartItem.quantity! -= 1;
      this.updateQuantity(cartItem);
    } else {
      this.toastrs.error('Số lượng phải lớn hơn 0 !!');
    }
  }
  ngOnInit(): void {
    this.user = this.authService.getAuthorization();
    this.onInit();
  }
  override search(page: any) {
    this.page.curentPage = page;
    this.cartDetailSearch.userId = this.user!.id;
    this.cartDetailService
      .searchPaging(this.page, this.cartDetailSearch)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.content) this.asyncData = res.data?.content;
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
        this.deleteProductInCart(id);
      }
    });
  }
  deleteProductInCart(id: any) {
    this.cartDetailService.delete(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        this.toastrs.success('Thành công');
        window.location.reload();
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
  async onPay() {
    this.isSubmit = true;
    if (this.billForm.status == 'INVALID') {
      this.focusInValidForm();
      return;
    } else if (this.billForm.status == 'PENDING') {
      this.toastrs.warning('Hệ thống đang xử lý! Vui lòng đợi');
      return;
    } else {
      await this.create();
    }
  }
  override async create() {
    this.bill.id = 1;
    this.bill.status = '1';
    this.bill.total = this.finalTotalPrice;
    let user = new User();
    user.id = this.user?.id;
    this.bill.user = user;
    this.billService.create(this.bill).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          if (!res.data?.billDetails) res.data!.billDetails = [];
          this.selection.selected.forEach((cart) => {
            let billDetail = new BillDetail();
            billDetail.quantity = cart.quantity;
            billDetail.giaoTrinh = cart.giaoTrinh;
            res.data?.billDetails?.push(billDetail);
          });
          this.bill = res.data!;
          this.update();
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
  override update() {
    this.billService.update(this.bill).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        this.router.navigate(['/bills', this.bill.id, 'payment']);
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
  updateQuantity(cartItem: CartDetail) {
    this.cartDetailService.update(cartItem).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          cartItem = res.data;
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
}
