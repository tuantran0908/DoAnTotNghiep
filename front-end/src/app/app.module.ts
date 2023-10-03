import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainNavbarComponent } from './component/layout-fixed/main-navbar/main-navbar.component';
import { MainSidebarComponent } from './component/layout-fixed/main-sidebar/main-sidebar.component';
import { LayoutFixedComponent } from './layout/layout-fixed/layout-fixed.component';
import { LoginComponent } from './page/login/login.component';
import { NguoiDungComponent } from './page/nguoi-dung/nguoi-dung.component';
import { PageNotFoundComponent } from './page/page-not-found/page-not-found.component';
import { PageHeaderComponent } from './component/layout-fixed/page-header/page-header.component';
import { PageFooterComponent } from './component/layout-fixed/page-footer/page-footer.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BackButtonDirective } from './directive/back-button/back-button.directive';
import { ChinhSuaNguoiDungComponent } from './page/nguoi-dung/chinh-sua-nguoi-dung/chinh-sua-nguoi-dung.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { CommonModule, DatePipe } from '@angular/common';
import { ToastrModule } from 'ngx-toastr';
import { LoaderService } from './services/loader/loader.service';
import { LoaderInterceptor } from './interceptor/loader.interceptor';
import { LoaderComponent } from './component/loader/loader.component';
import { HandleErrorsInterceptor } from './interceptor/handle-errors.interceptor';
import { NgselectComponent } from './component/ngselect/ngselect.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';
import { MatDialogModule } from '@angular/material/dialog';
import { commons } from './common/commons';
import {
  CustomAdapter,
  CustomDateParserFormatter,
} from './component/date-picker/datepicker';
import { ConfirmDialogComponent } from './component/confirm-dialog/confirm-dialog.component';
import { HomePageComponent } from './page/home-page/home-page.component';
import { ChiTietNguoiDungComponent } from './page/nguoi-dung/chi-tiet-nguoi-dung/chi-tiet-nguoi-dung.component';
import { BreadcrumbComponent } from './component/breadcrumb/breadcrumb.component';
import { NhomQuyenComponent } from './page/nhom-quyen-quyen/nhom-quyen/nhom-quyen.component';
import { QuyenComponent } from './page/nhom-quyen-quyen/quyen/quyen.component';
import { ChiTietQuyenComponent } from './page/nhom-quyen-quyen/quyen/chi-tiet-quyen/chi-tiet-quyen.component';
import { CreateUpdateQuyenComponent } from './page/nhom-quyen-quyen/quyen/create-update-quyen/create-update-quyen.component';
import { ChiTietNhomQuyenComponent } from './page/nhom-quyen-quyen/nhom-quyen/chi-tiet-nhom-quyen/chi-tiet-nhom-quyen.component';
import { CreateUpdateNhomQuyenComponent } from './page/nhom-quyen-quyen/nhom-quyen/create-update-nhom-quyen/create-update-nhom-quyen.component';
import { CategoryComponent } from './page/category/category.component';
import { ProductsComponent } from './page/products/products.component';
import { DetailCategoryComponent } from './page/category/detail-category/detail-category.component';
import { CreateUpdateCategoryComponent } from './page/category/create-update-category/create-update-category.component';
import { DetailProductsComponent } from './page/products/detail-products/detail-products.component';
import { CreateUpdateProductsComponent } from './page/products/create-update-products/create-update-products.component';
import { LayoutProductComponent } from './layout/layout-product/layout-product.component';
import { ProductNavbarComponent } from './component/layout-product/product-navbar/product-navbar.component';
import { ProductSidebarComponent } from './component/layout-product/product-sidebar/product-sidebar.component';
import { ProductHeaderComponent } from './component/layout-product/product-header/product-header.component';
import { ProductFooterComponent } from './component/layout-product/product-footer/product-footer.component';
import { CarouselModule } from 'ngx-owl-carousel-o';
import { CategoryProductComponent } from './page/category-product/category-product.component';
import { ProductDetailComponent } from './page/product-detail/product-detail.component';
import { CartDetailComponent } from './page/cart-detail/cart-detail.component';
import { ProfileComponent } from './page/profile/profile.component';
import { BillsComponent } from './page/bills/bills.component';
import { BillDetailComponent } from './page/bill-detail/bill-detail.component';
import { ReportComponent } from './page/report/report.component';
import { NgApexchartsModule } from 'ng-apexcharts';
import { ReviewDialogComponent } from './component/review-dialog/review-dialog.component';
import { CmsBillsComponent } from './page/cms-bills/cms-bills.component';
import { CmsReviewComponent } from './page/cms-review/cms-review.component';
import { DetailCmsBillsComponent } from './page/cms-bills/detail-cms-bills/detail-cms-bills.component';
import { CreateUpdateCmsBillsComponent } from './page/cms-bills/create-update-cms-bills/create-update-cms-bills.component';
import { CreateUpdateCmsReviewComponent } from './page/cms-review/create-update-cms-review/create-update-cms-review.component';
import { DetailCmsReviewComponent } from './page/cms-review/detail-cms-review/detail-cms-review.component';
import { AutosizeModule } from 'ngx-autosize';
import { PaymentComponent } from './page/payment/payment.component';
@NgModule({
  declarations: [
    AppComponent,
    MainNavbarComponent,
    MainSidebarComponent,
    LayoutFixedComponent,
    LoginComponent,
    NguoiDungComponent,
    PageNotFoundComponent,
    PageHeaderComponent,
    PageFooterComponent,
    BackButtonDirective,
    ChinhSuaNguoiDungComponent,
    LoginComponent,
    LoaderComponent,
    NgselectComponent,
    ChiTietNguoiDungComponent,
    ConfirmDialogComponent,
    HomePageComponent,
    BreadcrumbComponent,
    NhomQuyenComponent,
    QuyenComponent,
    ChiTietQuyenComponent,
    CreateUpdateQuyenComponent,
    ChiTietNhomQuyenComponent,
    CreateUpdateNhomQuyenComponent,
    CategoryComponent,
    ProductsComponent,
    DetailCategoryComponent,
    CreateUpdateCategoryComponent,
    DetailProductsComponent,
    CreateUpdateProductsComponent,
    LayoutProductComponent,
    ProductNavbarComponent,
    ProductSidebarComponent,
    ProductHeaderComponent,
    ProductFooterComponent,
    CategoryProductComponent,
    ProductDetailComponent,
    CartDetailComponent,
    ProfileComponent,
    BillsComponent,
    BillDetailComponent,
    ReportComponent,
    ReviewDialogComponent,
    CmsBillsComponent,
    CmsReviewComponent,
    DetailCmsBillsComponent,
    CreateUpdateCmsBillsComponent,
    DetailCmsReviewComponent,
    CreateUpdateCmsReviewComponent,
    PaymentComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    NgSelectModule,
    BrowserAnimationsModule,
    HttpClientModule,
    NgxPaginationModule,
    ReactiveFormsModule,
    CommonModule,
    BrowserAnimationsModule,
    MatDialogModule,
    ToastrModule.forRoot(),
    NgbModule,
    CarouselModule,
    NgApexchartsModule,
    AutosizeModule,
  ],
  bootstrap: [AppComponent],
  providers: [
    LoaderService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HandleErrorsInterceptor,
      multi: true,
    },
    { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true },
    { provide: NgbDateAdapter, useClass: CustomAdapter },
    { provide: NgbDateParserFormatter, useClass: CustomDateParserFormatter },
    commons,
    DatePipe,
  ],
})
export class AppModule {}
