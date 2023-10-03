import { Component, ElementRef, Injector, ViewChild } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import {
  debounceTime,
  distinctUntilChanged,
  filter,
  fromEvent,
  of,
  switchMap,
  tap,
  timer,
} from 'rxjs';
import { FwError, StatusEnum } from 'src/app/common/constants';
import {
  Category,
  GiaoTrinh,
  GiaoTrinhSearchRequest,
  LoginResponse,
  User,
} from 'src/app/model/model';
import { BaseComponent } from 'src/app/page/base/BaseComponent';
import { CategoryProductComponent } from 'src/app/page/category-product/category-product.component';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CartDetailService } from 'src/app/services/cart-detail/cart-detail.service';
import { CategoryService } from 'src/app/services/category/category.service';
import { ProductsService } from 'src/app/services/products/products.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-product-navbar',
  templateUrl: './product-navbar.component.html',
  styleUrls: ['./product-navbar.component.scss'],
})
export class ProductNavbarComponent extends BaseComponent<GiaoTrinh> {
  giaoTrinhSearch: GiaoTrinhSearchRequest = new GiaoTrinhSearchRequest();
  categorySearch: Category = new Category();
  searchController: string = '';
  categories: Category[] = [
    {
      id: undefined,
      name: 'Giáo trình giảm giá',
      url: '/category/sale-product',
      description: undefined,
      status: undefined,
      giaoTrinh: undefined,
      createdBy: undefined,
      createdAt: undefined,
      updatedBy: undefined,
      updatedAt: undefined,
    },
    {
      id: undefined,
      name: 'Giáo trình mới',
      url: '/category/new-product',
      description: undefined,
      status: undefined,
      giaoTrinh: undefined,
      createdBy: undefined,
      createdAt: undefined,
      updatedBy: undefined,
      updatedAt: undefined,
    },
    {
      id: undefined,
      name: 'Giáo trình cũ',
      url: '/category/old-product',
      description: undefined,
      status: undefined,
      giaoTrinh: undefined,
      createdBy: undefined,
      createdAt: undefined,
      updatedBy: undefined,
      updatedAt: undefined,
    },
    {
      id: undefined,
      name: 'Giáo trình bán chạy',
      url: '/category/best-seller',
      description: undefined,
      status: undefined,
      giaoTrinh: undefined,
      createdBy: undefined,
      createdAt: undefined,
      updatedBy: undefined,
      updatedAt: undefined,
    },
    {
      id: undefined,
      name: 'Giáo trình yêu thích',
      url: '/category/favorite-product',
      description: undefined,
      status: undefined,
      giaoTrinh: undefined,
      createdBy: undefined,
      createdAt: undefined,
      updatedBy: undefined,
      updatedAt: undefined,
    },
  ];
  products: GiaoTrinh[] = [];
  userLogin?: LoginResponse;
  user?: User;
  @ViewChild('input') input?: ElementRef;
  countCartDetail?: number;
  constructor(
    private productsService: ProductsService,
    public userService: UserService,
    public categoryService: CategoryService,
    private authService: AuthService,
    private cartDetailService: CartDetailService,
    private injector: Injector
  ) {
    super(injector);
  }
  ngOnInit(): void {
    this.userLogin = this.authService.getAuthorization();
    if (this.userLogin) this.getUser(this.userLogin.id);
    this.getCountCartDetail();
  }
  getUser(id: any) {
    this.userService.getDetail(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.user = res.data;
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
  public getCountCartDetail() {
    this.cartDetailService.getCount(this.userLogin!.id).subscribe((res) => {
      this.countCartDetail = res.data;
    });
  }
  ngAfterViewInit() {
    fromEvent(this.input!.nativeElement, 'keyup')
      .pipe(
        filter(Boolean),
        debounceTime(500),
        distinctUntilChanged(),
        tap((text) => {
          this.search(1);
        })
      )
      .subscribe();
  }
  override search(page: any) {
    this.page.curentPage = page;
    if (this.searchController) {
      this.categorySearch.name = this.searchController;
      this.categorySearch.status = StatusEnum.Y;
      this.giaoTrinhSearch.name = this.searchController;
      this.giaoTrinhSearch.status = StatusEnum.Y;
      this.categoryService
        .searchPaging(this.page, this.categorySearch)
        .subscribe((res) => {
          this.productsService
            .searchPaging(this.page, this.giaoTrinhSearch)
            .subscribe((res) => {
              if (FwError.THANHCONG == res.errorCode) {
                if (res.data?.content) this.products = res.data?.content;
              } else {
                this.toastrs.error(res.errorMessage);
              }
            });
          if (FwError.THANHCONG == res.errorCode) {
            if (res.data?.content) this.categories = res.data!.content;
          } else {
            this.toastrs.error(res.errorMessage);
          }
        });
    } else {
      this.categories = [
        {
          id: undefined,
          name: 'Giáo trình giảm giá',
          url: '/category/sale-product',
          description: undefined,
          status: undefined,
          giaoTrinh: undefined,
          createdBy: undefined,
          createdAt: undefined,
          updatedBy: undefined,
          updatedAt: undefined,
        },
        {
          id: undefined,
          name: 'Giáo trình mới',
          url: '/category/new-product',
          description: undefined,
          status: undefined,
          giaoTrinh: undefined,
          createdBy: undefined,
          createdAt: undefined,
          updatedBy: undefined,
          updatedAt: undefined,
        },
        {
          id: undefined,
          name: 'Giáo trình cũ',
          url: '/category/old-product',
          description: undefined,
          status: undefined,
          giaoTrinh: undefined,
          createdBy: undefined,
          createdAt: undefined,
          updatedBy: undefined,
          updatedAt: undefined,
        },
        {
          id: undefined,
          name: 'Giáo trình bán chạy',
          url: '/category/best-seller',
          description: undefined,
          status: undefined,
          giaoTrinh: undefined,
          createdBy: undefined,
          createdAt: undefined,
          updatedBy: undefined,
          updatedAt: undefined,
        },
        {
          id: undefined,
          name: 'Giáo trình yêu thích',
          url: '/category/favorite-product',
          description: undefined,
          status: undefined,
          giaoTrinh: undefined,
          createdBy: undefined,
          createdAt: undefined,
          updatedBy: undefined,
          updatedAt: undefined,
        },
      ];
      this.products = [];
    }
  }
}
