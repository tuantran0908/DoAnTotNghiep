import { Component, Injector } from '@angular/core';
import {
  GiaoTrinh,
  Review,
  ReviewSearchRequest,
  User,
} from 'src/app/model/model';
import { BaseComponent } from '../base/BaseComponent';
import { ReviewService } from 'src/app/services/review/review.service';
import { MatDialog } from '@angular/material/dialog';
import { FormControl } from '@angular/forms';
import { FwError } from 'src/app/common/constants';
import { ConfirmDialogComponent } from 'src/app/component/confirm-dialog/confirm-dialog.component';
import { UserService } from 'src/app/services/user/user.service';
import { ProductsService } from 'src/app/services/products/products.service';

@Component({
  selector: 'app-cms-review',
  templateUrl: './cms-review.component.html',
  styleUrls: ['./cms-review.component.scss'],
})
export class CmsReviewComponent extends BaseComponent<Review> {
  reviewSearch: ReviewSearchRequest = new ReviewSearchRequest();
  constructor(
    private reviewService: ReviewService,
    public userService: UserService,
    public productsService: ProductsService,
    private dialog: MatDialog,
    private injector: Injector
  ) {
    super(injector);
  }
  searchForm = this.formBuilder.group({
    star: new FormControl(),
    message: new FormControl(),
    user: new FormControl(),
    giaoTrinh: new FormControl(),
  });
  usersSelected?: User[];
  productsSelected?: GiaoTrinh[];

  ngOnInit(): void {
    this.onInit();
  }

  override search(page: any) {
    this.page.curentPage = page;
    if (this.usersSelected && this.usersSelected.length > 0) {
      this.reviewSearch.userId = this.usersSelected
        .map((item) => item.id)
        .toString();
    }
    if (this.productsSelected && this.productsSelected.length > 0) {
      this.reviewSearch.giaoTrinhId = this.productsSelected
        .map((item) => item.id)
        .toString();
    }
    this.reviewService
      .searchPaging(this.page, this.reviewSearch)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.totalElements) this.total = res.data.totalElements;
          if (res.data?.content) this.asyncData = res.data.content;
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }
  delete(id: any) {
    this.reviewService.delete(id).subscribe((res) => {
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
  onChecked(value: number) {
    this.reviewSearch.star = value;
  }
}
