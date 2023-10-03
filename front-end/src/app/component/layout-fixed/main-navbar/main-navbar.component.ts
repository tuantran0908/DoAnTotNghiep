import { Component, OnInit } from '@angular/core';
import { FwError } from 'src/app/common/constants';
import { LoginResponse, User } from 'src/app/model/model';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-main-navbar',
  templateUrl: './main-navbar.component.html',
  styleUrls: ['./main-navbar.component.scss'],
})
export class MainNavbarComponent implements OnInit {
  userLogin?: LoginResponse;
  user?: User;
  constructor(
    private authService: AuthService,
    public userService: UserService
  ) {}

  ngOnInit(): void {
    this.userLogin = this.authService.getAuthorization();
    if (this.userLogin) this.getUser(this.userLogin.id);
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
}
