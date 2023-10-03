import { Component, Injector } from '@angular/core';
import { FwError } from 'src/app/common/constants';
import { User } from 'src/app/model/model';
import { BaseComponent } from 'src/app/page/base/BaseComponent';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-chi-tiet-nguoi-dung',
  templateUrl: './chi-tiet-nguoi-dung.component.html',
  styleUrls: ['./chi-tiet-nguoi-dung.component.scss'],
})
export class ChiTietNguoiDungComponent extends BaseComponent<User> {
  userDetail: User = new User();
  constructor(private userService: UserService, private injector: Injector) {
    super(injector);
  }
  ngOnInit(): void {
    this.onInit();
  }
  override getDetail(id: any) {
    this.userService.getDetail(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.userDetail = res.data;
          this.userDetail.status == '1' &&
            (this.userDetail.status = 'Hiệu lực');
          this.userDetail.status == '0' &&
            (this.userDetail.status = 'Không hiệu lực');
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
}
