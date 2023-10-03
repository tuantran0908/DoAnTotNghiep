import { Component, Injector } from '@angular/core';
import {
  Banner,
  CartDetail,
  GiaoTrinh,
  GiaoTrinhSearchRequest,
  User,
} from 'src/app/model/model';
import { ProductsService } from 'src/app/services/products/products.service';
import { BaseComponent } from '../base/BaseComponent';
import { OwlOptions } from 'ngx-owl-carousel-o';
import { FwError, StatusEnum } from 'src/app/common/constants';
import { CategoryService } from 'src/app/services/category/category.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CartDetailService } from 'src/app/services/cart-detail/cart-detail.service';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-category-product',
  templateUrl: './category-product.component.html',
  styleUrls: ['./category-product.component.scss'],
})
export class CategoryProductComponent extends BaseComponent<GiaoTrinh> {
  banners: Banner[] = [
    {
      id: 1,
      url: 'assets/images/banner_2.jpg',
      name: 'Banner 1',
      type: '1',
      status: '1',
      createdAt: undefined,
      createdBy: undefined,
      updatedAt: undefined,
      updatedBy: undefined,
    },
    {
      id: 2,
      url: 'assets/images/banner_3.jpg',
      name: 'Banner 1',
      type: '1',
      status: '1',
      createdAt: undefined,
      createdBy: undefined,
      updatedAt: undefined,
      updatedBy: undefined,
    },
    {
      id: 3,
      url: 'assets/images/banner_4.jpg',
      name: 'Banner 1',
      type: '1',
      status: '1',
      createdAt: undefined,
      createdBy: undefined,
      updatedAt: undefined,
      updatedBy: undefined,
    },
    {
      id: 4,
      url: 'assets/images/banner_5.jpg',
      name: 'Banner 1',
      type: '1',
      status: '1',
      createdAt: undefined,
      createdBy: undefined,
      updatedAt: undefined,
      updatedBy: undefined,
    },
    {
      id: 5,
      url: 'assets/images/banner_6.jpg',
      name: 'Banner 1',
      type: '1',
      status: '1',
      createdAt: undefined,
      createdBy: undefined,
      updatedAt: undefined,
      updatedBy: undefined,
    },
  ];
  customOptionsBanner: OwlOptions = {
    loop: true,
    autoplay: true,
    center: true,
    dotsEach: true,
    autoHeight: true,
    autoWidth: true,
    autoplayHoverPause: true,
    autoplayMouseleaveTimeout: 500,
    autoplaySpeed: 500,
    responsive: {
      0: {
        items: 1,
      },
      600: {
        items: 1,
      },
      1000: {
        items: 1,
      },
    },
  };
  titleCategory?: String;
  typeSearch?: String;
  optionSearch: String = 'lien-quan';
  giaoTrinhSearch: GiaoTrinhSearchRequest = new GiaoTrinhSearchRequest();
  searchForm = this.formBuilder.group({
    name: new FormControl(),
    author: new FormControl(),
    priceFrom: new FormControl(),
    priceTo: new FormControl(),
  });
  constructor(
    private productsService: ProductsService,
    private categoryService: CategoryService,
    private authService: AuthService,
    private cartDetailService: CartDetailService,
    private injector: Injector
  ) {
    super(injector);
  }
  ngOnInit(): void {
    this.typeSearch = this.activatedRoute.snapshot.data['type'];
    this.onInit();
  }
  override search(page: any) {
    this.getCategoryDetail();
    this.page.curentPage = page;
    if (this.typeSearch == 'sale-product') {
      this.titleCategory = 'GIÁO TRÌNH GIẢM GIÁ';
      this.giaoTrinhSearch.saleProduct = true;
    }
    if (this.typeSearch == 'new-product') {
      this.titleCategory = 'GIÁO TRÌNH MỚI';
      this.giaoTrinhSearch.isNew = StatusEnum.Y;
    }
    if (this.typeSearch == 'old-product') {
      this.titleCategory = 'GIÁO TRÌNH CŨ';
      this.giaoTrinhSearch.isNew = StatusEnum.N;
    }
    if (this.typeSearch == 'best-seller') {
      this.titleCategory = 'GIÁO TRÌNH BÁN CHẠY';
      this.giaoTrinhSearch.bestSeller = true;
    }
    if (this.typeSearch == 'favorite-product') {
      this.titleCategory = 'GIÁO TRÌNH YÊU THÍCH';
      this.giaoTrinhSearch.saleProduct = true;
    }
    if (this.typeSearch == 'categoryIds') {
      this.giaoTrinhSearch.categoryIds =
        this.activatedRoute.snapshot.paramMap.get('id') ?? undefined;
    }
    if (this.optionSearch == 'lien-quan') {
      this.giaoTrinhSearch.lastestProduct = false;
      this.giaoTrinhSearch.bestSeller = false;
    }
    if (this.optionSearch == 'moi-nhat') {
      this.giaoTrinhSearch.bestSeller = false;
      this.giaoTrinhSearch.lastestProduct = true;
    }
    if (this.optionSearch == 'ban-chay') {
      this.giaoTrinhSearch.lastestProduct = false;
      this.giaoTrinhSearch.bestSeller = true;
    }
    this.page.size = 12;
    this.productsService
      .searchPaging(this.page, this.giaoTrinhSearch)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.totalElements) this.total = res.data.totalElements;
          if (res.data?.content) this.asyncData = res.data.content;
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }
  getCategoryDetail() {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id)
      this.categoryService.getDetail(id).subscribe((roles) => {
        if (FwError.THANHCONG == roles.errorCode) {
          if (roles.data) this.titleCategory = roles.data.name;
        } else {
          this.toastrs.error(roles.errorMessage);
        }
      });
  }
  onClickOptionSearch(optionSearch: String) {
    if (this.optionSearch != optionSearch) {
      this.optionSearch = optionSearch;
      this.search(this.page.curentPage);
    }
  }
  onClickAddToCart(giaoTrinh: GiaoTrinh) {
    let newCartItem = new CartDetail();
    newCartItem.giaoTrinh = giaoTrinh;
    newCartItem.quantity = 1;
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
  onChecked(value: number) {
    this.giaoTrinhSearch.star = value;
  }
  onClear() {
    this.giaoTrinhSearch.star = undefined;
    this.giaoTrinhSearch.author = undefined;
    this.giaoTrinhSearch.name = undefined;
    this.giaoTrinhSearch.priceFrom = undefined;
    this.giaoTrinhSearch.priceTo = undefined;
    this.search(this.page.curentPage);
  }
}
