import { Component, Injector } from '@angular/core';
import { BaseComponent } from '../base/BaseComponent';
import {
  CartDetail,
  GiaoTrinh,
  Review,
  ReviewSearchRequest,
  User,
} from 'src/app/model/model';
import { ProductsService } from 'src/app/services/products/products.service';
import { FwError } from 'src/app/common/constants';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CartDetailService } from 'src/app/services/cart-detail/cart-detail.service';
import { ReviewService } from 'src/app/services/review/review.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
})
export class ProductDetailComponent extends BaseComponent<Review> {
  product: GiaoTrinh = new GiaoTrinh();
  quantity: number = 1;
  avgStar?: number;
  reviewSearch: ReviewSearchRequest = new ReviewSearchRequest();
  optionSearch: String = 'tat-ca';
  id?: String;
  constructor(
    private productsService: ProductsService,
    private reviewService: ReviewService,
    private cartDetailService: CartDetailService,
    private authService: AuthService,
    private injector: Injector
  ) {
    super(injector);
  }
  ngOnInit(): void {
    this.onInit();
  }
  getReview(id: any) {
    this.getReviewSearchPaging(1, id);
  }
  getReviewSearchPaging(page: any, id: any) {
    this.page.curentPage = page;
    this.page.size = 5;
    this.reviewSearch.giaoTrinhId = id;
    if (this.optionSearch == 'tat-ca') {
      this.reviewSearch.star = undefined;
    }
    if (this.optionSearch == '5-sao') {
      this.reviewSearch.star = 5;
    }
    if (this.optionSearch == '4-sao') {
      this.reviewSearch.star = 4;
    }
    if (this.optionSearch == '3-sao') {
      this.reviewSearch.star = 3;
    }
    if (this.optionSearch == '2-sao') {
      this.reviewSearch.star = 2;
    }
    if (this.optionSearch == '1-sao') {
      this.reviewSearch.star = 1;
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
  override getDetail(id: any) {
    this.id = id;
    this.getReview(id);
    this.productsService.getDetail(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.product = res.data;
          this.avgStar = res.data.avgStar;
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
  onClickOptionSearch(optionSearch: String) {
    if (this.optionSearch != optionSearch) {
      this.optionSearch = optionSearch;
      this.getReviewSearchPaging(this.page.curentPage, this.id);
    }
  }
  onClickAdd(giaoTrinh: GiaoTrinh) {
    if (this.quantity < giaoTrinh.quantityRemaining!) {
      this.quantity += 1;
    } else {
      this.toastrs.error('Vượt quá số lượng có sẵn trong kho !!');
    }
  }
  onClickSub(giaoTrinh: GiaoTrinh) {
    if (this.quantity > 1) {
      this.quantity -= 1;
    } else {
      this.toastrs.error('Số lượng phải lớn hơn 0 !!');
    }
  }
  onClickAddToCart(giaoTrinh: GiaoTrinh) {
    let newCartItem = new CartDetail();
    newCartItem.giaoTrinh = giaoTrinh;
    newCartItem.quantity = this.quantity;
    let user = new User();
    if (this.authService.getAuthorization()) {
      user.id = this.authService.getAuthorization().id;
    }
    newCartItem.user = user;
    newCartItem.id = 1;
    this.cartDetailService.create(newCartItem).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.toastrs.success('Thêm vào giỏ hàng thành công');
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
}
