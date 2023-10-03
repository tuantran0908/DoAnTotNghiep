import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LayoutFixedComponent } from './layout/layout-fixed/layout-fixed.component';
import { LoginComponent } from './page/login/login.component';
import { NguoiDungComponent } from './page/nguoi-dung/nguoi-dung.component';
import { PageNotFoundComponent } from './page/page-not-found/page-not-found.component';
import { ChinhSuaNguoiDungComponent } from './page/nguoi-dung/chinh-sua-nguoi-dung/chinh-sua-nguoi-dung.component';
import { AuthGuard } from './auth/auth.guard';
import { FwAction } from './common/constants';
import { User } from './model/model';
import { HomePageComponent } from './page/home-page/home-page.component';
import { ChiTietNguoiDungComponent } from './page/nguoi-dung/chi-tiet-nguoi-dung/chi-tiet-nguoi-dung.component';
import { NhomQuyenComponent } from './page/nhom-quyen-quyen/nhom-quyen/nhom-quyen.component';
import { ChiTietNhomQuyenComponent } from './page/nhom-quyen-quyen/nhom-quyen/chi-tiet-nhom-quyen/chi-tiet-nhom-quyen.component';
import { CreateUpdateNhomQuyenComponent } from './page/nhom-quyen-quyen/nhom-quyen/create-update-nhom-quyen/create-update-nhom-quyen.component';
import { QuyenComponent } from './page/nhom-quyen-quyen/quyen/quyen.component';
import { ChiTietQuyenComponent } from './page/nhom-quyen-quyen/quyen/chi-tiet-quyen/chi-tiet-quyen.component';
import { CreateUpdateQuyenComponent } from './page/nhom-quyen-quyen/quyen/create-update-quyen/create-update-quyen.component';
import { CategoryComponent } from './page/category/category.component';
import { DetailCategoryComponent } from './page/category/detail-category/detail-category.component';
import { CreateUpdateCategoryComponent } from './page/category/create-update-category/create-update-category.component';
import { ProductsComponent } from './page/products/products.component';
import { DetailProductsComponent } from './page/products/detail-products/detail-products.component';
import { CreateUpdateProductsComponent } from './page/products/create-update-products/create-update-products.component';
import { LayoutProductComponent } from './layout/layout-product/layout-product.component';
import { CategoryProductComponent } from './page/category-product/category-product.component';
import { ProductDetailComponent } from './page/product-detail/product-detail.component';
import { CartDetailComponent } from './page/cart-detail/cart-detail.component';
import { ProfileComponent } from './page/profile/profile.component';
import { BillsComponent } from './page/bills/bills.component';
import { BillDetailComponent } from './page/bill-detail/bill-detail.component';
import { ReportComponent } from './page/report/report.component';
import { CmsBillsComponent } from './page/cms-bills/cms-bills.component';
import { DetailCmsBillsComponent } from './page/cms-bills/detail-cms-bills/detail-cms-bills.component';
import { CreateUpdateCmsBillsComponent } from './page/cms-bills/create-update-cms-bills/create-update-cms-bills.component';
import { CmsReviewComponent } from './page/cms-review/cms-review.component';
import { DetailCmsReviewComponent } from './page/cms-review/detail-cms-review/detail-cms-review.component';
import { CreateUpdateCmsReviewComponent } from './page/cms-review/create-update-cms-review/create-update-cms-review.component';
import { PaymentComponent } from './page/payment/payment.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: '',
    component: LayoutProductComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'home-page' },
      {
        path: 'home-page',
        data: { action: FwAction.SEARCH, breadcrumb: 'Trang chủ' },
        component: HomePageComponent,
        title: 'Trang chủ',
      },
      {
        path: 'category',
        children: [
          {
            path: 'sale-product',
            data: { action: FwAction.SEARCH, type: 'sale-product' },
            component: CategoryProductComponent,
            title: 'Giáo trình giảm giá',
          },
          {
            path: 'new-product',
            data: { action: FwAction.SEARCH, type: 'new-product' },
            component: CategoryProductComponent,
            title: 'Giáo trình mới',
          },
          {
            path: 'old-product',
            data: { action: FwAction.SEARCH, type: 'old-product' },
            component: CategoryProductComponent,
            title: 'Giáo trình cũ',
          },
          {
            path: 'best-seller',
            data: { action: FwAction.SEARCH, type: 'best-seller' },
            component: CategoryProductComponent,
            title: 'Giáo trình bán chạy',
          },
          {
            path: 'favorite-product',
            data: { action: FwAction.SEARCH, type: 'favorite-product' },
            component: CategoryProductComponent,
            title: 'Giáo trình yêu thích',
          },
          {
            path: ':id',
            data: { action: FwAction.SEARCH, type: 'categoryIds' },
            component: CategoryProductComponent,
            title: 'Giáo trình theo loại',
          },
        ],
      },
      {
        path: 'product',
        children: [
          {
            path: ':id',
            data: { action: FwAction.DETAIL },
            component: ProductDetailComponent,
            title: 'Giáo trình',
          },
        ],
      },
      {
        path: 'cart',
        data: { action: FwAction.SEARCH },
        component: CartDetailComponent,
        title: 'Giỏ hàng của tôi',
      },
      {
        path: 'profile/:id',
        data: { action: FwAction.UPDATE },
        component: ProfileComponent,
        title: 'Thông tin cá nhân',
      },
      {
        path: 'bills',
        children: [
          {
            path: '',
            data: { action: FwAction.SEARCH },
            component: BillsComponent,
            title: 'Đơn hàng của tôi',
          },
          {
            path: ':id',
            data: { action: FwAction.DETAIL },
            component: BillDetailComponent,
            title: 'Chi tiết đơn hàng',
          },
          {
            path: ':id/payment',
            data: { action: FwAction.DETAIL },
            component: PaymentComponent,
            title: 'Thanh toán',
          },
        ],
      },
    ],
  },
  {
    path: 'cms',
    component: LayoutFixedComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'report' },
      {
        path: 'report',
        data: { action: FwAction.SEARCH, breadcrumb: 'Thống kê, báo cáo' },
        component: ReportComponent,
        title: 'Thống kê, báo cáo',
      },
      {
        path: 'users',
        data: { action: FwAction.SEARCH, breadcrumb: 'Quản trị người dùng' },
        component: NguoiDungComponent,
        title: 'Người dùng',
      },
      {
        path: 'users/detail/:id',
        data: {
          action: FwAction.DETAIL,
          breadcrumb: 'Chi tiết người dùng',
        },
        component: ChiTietNguoiDungComponent,
        title: 'Chi tiết người dùng',
      },
      {
        path: 'users/create',
        data: { action: FwAction.CREATE, breadcrumb: 'Thêm mới người dùng' },
        component: ChinhSuaNguoiDungComponent,
        title: 'Thêm mới người dùng',
      },
      {
        path: 'users/update/:id',
        data: { action: FwAction.UPDATE, breadcrumb: 'Chỉnh sửa người dùng' },
        component: ChinhSuaNguoiDungComponent,
        title: 'Chỉnh sửa người dùng',
      },
      {
        path: 'role-group',
        data: { action: FwAction.SEARCH, breadcrumb: 'Quản trị nhóm quyền' },
        component: NhomQuyenComponent,
        title: 'Nhóm quyền',
      },
      {
        path: 'role-group/detail/:id',
        data: {
          action: FwAction.DETAIL,
          breadcrumb: 'Chi tiết nhóm quyền',
        },
        component: ChiTietNhomQuyenComponent,
        title: 'Chi tiết nhóm quyền',
      },
      {
        path: 'role-group/create',
        data: { action: FwAction.CREATE, breadcrumb: 'Thêm mới nhóm quyền' },
        component: CreateUpdateNhomQuyenComponent,
        title: 'Thêm mới nhóm quyền',
      },
      {
        path: 'role-group/update/:id',
        data: { action: FwAction.UPDATE, breadcrumb: 'Chỉnh sửa nhóm quyền' },
        component: CreateUpdateNhomQuyenComponent,
        title: 'Chỉnh sửa nhóm quyền',
      },
      {
        path: 'roles',
        data: { action: FwAction.SEARCH, breadcrumb: 'Quản trị quyền' },
        component: QuyenComponent,
        title: 'Quyền',
      },
      {
        path: 'roles/detail/:id',
        data: {
          action: FwAction.DETAIL,
          breadcrumb: 'Chi tiết quyền',
        },
        component: ChiTietQuyenComponent,
        title: 'Chi tiết quyền',
      },
      {
        path: 'roles/create',
        data: { action: FwAction.CREATE, breadcrumb: 'Thêm mới quyền' },
        component: CreateUpdateQuyenComponent,
        title: 'Thêm mới quyền',
      },
      {
        path: 'roles/update/:id',
        data: { action: FwAction.UPDATE, breadcrumb: 'Chỉnh sửa quyền' },
        component: CreateUpdateQuyenComponent,
        title: 'Chỉnh sửa quyền',
      },
      {
        path: 'category',
        data: {
          action: FwAction.SEARCH,
          breadcrumb: 'Quản trị loại giáo trình',
        },
        component: CategoryComponent,
        title: 'Loại giáo trình',
      },
      {
        path: 'category/detail/:id',
        data: {
          action: FwAction.DETAIL,
          breadcrumb: 'Chi tiết loại giáo trình',
        },
        component: DetailCategoryComponent,
        title: 'Chi tiết loại giáo trình',
      },
      {
        path: 'category/create',
        data: {
          action: FwAction.CREATE,
          breadcrumb: 'Thêm mới loại giáo trình',
        },
        component: CreateUpdateCategoryComponent,
        title: 'Thêm mới loại giáo trình',
      },
      {
        path: 'category/update/:id',
        data: {
          action: FwAction.UPDATE,
          breadcrumb: 'Chỉnh sửa loại giáo trình',
        },
        component: CreateUpdateCategoryComponent,
        title: 'Chỉnh sửa loại giáo trình',
      },
      {
        path: 'products',
        data: { action: FwAction.SEARCH, breadcrumb: 'Quản trị giáo trình' },
        component: ProductsComponent,
        title: 'Giáo trình',
      },
      {
        path: 'products/detail/:id',
        data: {
          action: FwAction.DETAIL,
          breadcrumb: 'Chi tiết giáo trình',
        },
        component: DetailProductsComponent,
        title: 'Chi tiết giáo trình',
      },
      {
        path: 'products/create',
        data: { action: FwAction.CREATE, breadcrumb: 'Thêm mới giáo trình' },
        component: CreateUpdateProductsComponent,
        title: 'Thêm mới giáo trình',
      },
      {
        path: 'products/update/:id',
        data: { action: FwAction.UPDATE, breadcrumb: 'Chỉnh sửa giáo trình' },
        component: CreateUpdateProductsComponent,
        title: 'Chỉnh sửa giáo trình',
      },
      {
        path: 'bills',
        data: { action: FwAction.SEARCH, breadcrumb: 'Quản trị đơn hàng' },
        component: CmsBillsComponent,
        title: 'Đơn hàng',
      },
      {
        path: 'bills/detail/:id',
        data: {
          action: FwAction.DETAIL,
          breadcrumb: 'Chi tiết đơn hàng',
        },
        component: DetailCmsBillsComponent,
        title: 'Chi tiết đơn hàng',
      },
      {
        path: 'bills/update/:id',
        data: { action: FwAction.UPDATE, breadcrumb: 'Chỉnh sửa đơn hàng' },
        component: CreateUpdateCmsBillsComponent,
        title: 'Chỉnh sửa đơn hàng',
      },
      {
        path: 'reviews',
        data: { action: FwAction.SEARCH, breadcrumb: 'Quản trị đánh giá' },
        component: CmsReviewComponent,
        title: 'Đánh giá giáo trình',
      },
      {
        path: 'reviews/detail/:id',
        data: {
          action: FwAction.DETAIL,
          breadcrumb: 'Chi tiết đánh giá giáo trình',
        },
        component: DetailCmsReviewComponent,
        title: 'Chi tiết đánh giá giáo trình',
      },
      {
        path: 'reviews/update/:id',
        data: {
          action: FwAction.UPDATE,
          breadcrumb: 'Chỉnh sửa đánh giá giáo trình',
        },
        component: CreateUpdateCmsReviewComponent,
        title: 'Chỉnh sửa đánh giá giáo trình',
      },
    ],
  },
  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
