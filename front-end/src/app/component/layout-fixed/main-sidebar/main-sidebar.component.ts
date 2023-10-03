import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FwError } from 'src/app/common/constants';
import { LoginResponse, User } from 'src/app/model/model';
import {
  SideBarGroup,
  SideBarCmsGroups,
  SideBarItem,
} from 'src/app/model/sidebar';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UserService } from 'src/app/services/user/user.service';
@Component({
  selector: 'app-main-sidebar',
  templateUrl: './main-sidebar.component.html',
  styleUrls: ['./main-sidebar.component.scss'],
})
export class MainSidebarComponent implements OnInit {
  user?: User;
  sideBarGroups: SideBarGroup[] = SideBarCmsGroups;
  constructor(
    private router: Router,
    private userService: UserService,
    private authService: AuthService
  ) {}
  ngOnInit(): void {
    const user = this.authService.getAuthorization();
    if (user) {
      this.getUser(user.id);
    }
    this.sideBarGroups.forEach((sideBarGroup) => {
      sideBarGroup.sideBarItems.forEach((sideBarItem) => {
        if (this.router.url.includes(sideBarItem.url)) {
          sideBarItem.isSelected = true;
          sideBarGroup.isExpanded = true;
        }
      });
    });
  }
  getUser(id: any) {
    this.userService.getDetail(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.user = res.data;
        }
      }
    });
  }
  onClickSideBarItem(sideBarItem: SideBarItem, sideBarGroup: SideBarGroup) {
    this.sideBarGroups.forEach((sideBarGroup) => {
      sideBarGroup.isExpanded = false;
      sideBarGroup.sideBarItems.forEach((sideBarItem) => {
        sideBarItem.isSelected = false;
      });
    });

    sideBarItem.isSelected = !sideBarItem.isSelected;
    sideBarGroup.isExpanded = !sideBarGroup.isExpanded;
  }
}
