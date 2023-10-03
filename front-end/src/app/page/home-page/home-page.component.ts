import { Component, Injector } from '@angular/core';
import { OwlOptions } from 'ngx-owl-carousel-o';
import {
  Banner,
  CartDetail,
  GiaoTrinh,
  GiaoTrinhSearchRequest,
  PagesRequest,
  ProductsGroup,
  User,
} from 'src/app/model/model';
import { BaseComponent } from '../base/BaseComponent';
import { ProductsService } from 'src/app/services/products/products.service';
import { CategoryService } from 'src/app/services/category/category.service';
import { FwError, StatusEnum } from 'src/app/common/constants';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CartDetailService } from 'src/app/services/cart-detail/cart-detail.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss'],
})
export class HomePageComponent extends BaseComponent<GiaoTrinh> {
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
  productsGroupList: ProductsGroup[] = [];
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
    this.getInitPage();
  }
  async getInitPage() {
    await this.getMainList();
    await this.getCategoryList();
  }
  async getMainList() {
    let page = new PagesRequest();
    page.size = 6;
    page.curentPage = 1;
    await this.getSaleProduct(page);
    await this.getNewProduct(page);
    await this.getOldProduct(page);
    await this.getBestSeller(page);
    await this.getFavoriteProduct(page);
  }

  async getSaleProduct(page: any) {
    let giaoTrinhSearchRequest = new GiaoTrinhSearchRequest();
    giaoTrinhSearchRequest.saleProduct = true;
    this.productsService
      .searchPaging(page, giaoTrinhSearchRequest)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.content) {
            let productsGroup = new ProductsGroup();
            productsGroup.title = 'GIÁO TRÌNH GIẢM GIÁ';
            productsGroup.url = '/category/sale-product';
            productsGroup.products = res.data!.content;
            this.productsGroupList.push(productsGroup);
          }
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }
  async getOldProduct(page: any) {
    let giaoTrinhSearchRequest = new GiaoTrinhSearchRequest();
    giaoTrinhSearchRequest.isNew = StatusEnum.N;
    this.productsService
      .searchPaging(page, giaoTrinhSearchRequest)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.content) {
            let productsGroup = new ProductsGroup();
            productsGroup.title = 'GIÁO TRÌNH CŨ';
            productsGroup.url = '/category/old-product';
            productsGroup.products = res.data!.content;
            this.productsGroupList.push(productsGroup);
          }
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }

  async getNewProduct(page: any) {
    let giaoTrinhSearchRequest = new GiaoTrinhSearchRequest();
    giaoTrinhSearchRequest.isNew = StatusEnum.Y;
    this.productsService
      .searchPaging(page, giaoTrinhSearchRequest)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.content) {
            let productsGroup = new ProductsGroup();
            productsGroup.title = 'GIÁO TRÌNH MỚI';
            productsGroup.url = '/category/new-product';
            productsGroup.products = res.data!.content;
            this.productsGroupList.push(productsGroup);
          }
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }

  async getBestSeller(page: any) {
    let giaoTrinhSearchRequest = new GiaoTrinhSearchRequest();
    giaoTrinhSearchRequest.bestSeller = true;
    this.productsService
      .searchPaging(page, giaoTrinhSearchRequest)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.content) {
            let productsGroup = new ProductsGroup();
            productsGroup.title = 'GIÁO TRÌNH BÁN CHẠY';
            productsGroup.url = '/category/best-seller';

            productsGroup.products = res.data!.content;
            this.productsGroupList.push(productsGroup);
          }
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }
  async getFavoriteProduct(page: any) {
    let giaoTrinhSearchRequest = new GiaoTrinhSearchRequest();
    giaoTrinhSearchRequest.favoriteProduct = true;
    this.productsService
      .searchPaging(page, giaoTrinhSearchRequest)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.content) {
            let productsGroup = new ProductsGroup();
            productsGroup.title = 'GIÁO TRÌNH YÊU THÍCH';
            productsGroup.url = '/category/favorite-product';
            productsGroup.products = res.data!.content;
            this.productsGroupList.push(productsGroup);
          }
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }
  async getCategoryList() {
    this.categoryService.getAllCategory().subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          res.data.forEach((category) => {
            let page = new PagesRequest();
            page.size = 6;
            page.curentPage = 1;
            let giaoTrinhSearchRequest = new GiaoTrinhSearchRequest();
            if (category.id)
              giaoTrinhSearchRequest.categoryIds = category.id.toString();
            this.productsService
              .searchPaging(page, giaoTrinhSearchRequest)
              .subscribe((res) => {
                if (FwError.THANHCONG == res.errorCode) {
                  if (res.data?.content) {
                    let productsGroup = new ProductsGroup();
                    productsGroup.title = category.name;
                    productsGroup.products = res.data!.content;
                    productsGroup.url = '/category/' + category.id;
                    this.productsGroupList.push(productsGroup);
                  }
                } else {
                  this.toastrs.error(res.errorMessage);
                }
              });
          });
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
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
}
