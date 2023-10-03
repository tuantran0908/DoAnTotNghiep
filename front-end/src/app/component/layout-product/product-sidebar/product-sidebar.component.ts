import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { FwError } from 'src/app/common/constants';
import { LoginResponse, User } from 'src/app/model/model';
import { SideBarItem, SideBarProductList } from 'src/app/model/sidebar';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CategoryService } from 'src/app/services/category/category.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-product-sidebar',
  templateUrl: './product-sidebar.component.html',
  styleUrls: ['./product-sidebar.component.scss'],
})
export class ProductSidebarComponent {
  SideBarProductList: SideBarItem[] = [];
  user?: User;
  constructor(
    private router: Router,
    private categoryService: CategoryService,
    private userService: UserService,
    private authService: AuthService,
    private toastrs: ToastrService
  ) {}
  ngOnInit(): void {
    const user = this.authService.getAuthorization();
    if (user) {
      this.getUser(user.id);
    }
    this.getCategoryList();
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
  getCategoryList() {
    SideBarProductList.forEach((element) => {
      this.SideBarProductList.push(element);
    });
    this.categoryService.getAllCategory().subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          res.data.forEach((category) => {
            let sideBarItem: SideBarItem = {
              id: category.id!.toString(),
              title: category.name!,
              url: '/category/' + category.id!,
              isSelected: false,
              type: 2,
            };
            this.SideBarProductList.push(sideBarItem);
          });
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
    this.SideBarProductList.forEach((SideBarProduct) => {
      if (this.router.url.includes(SideBarProduct.url)) {
        SideBarProduct.isSelected = true;
      }
    });
  }
  onClickSideBarItem(sideBarItem: SideBarItem) {
    SideBarProductList.forEach((sideBarItem) => {
      sideBarItem.isSelected = false;
    });
    sideBarItem.isSelected = !sideBarItem.isSelected;
  }
  filterItemsOfType(type: number) {
    return this.SideBarProductList.filter((item) => item.type == type);
  }
}
