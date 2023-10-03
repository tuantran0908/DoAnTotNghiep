import { Component, Injector } from '@angular/core';
import { FwError } from 'src/app/common/constants';
import { Roles } from 'src/app/model/model';
import { BaseComponent } from 'src/app/page/base/BaseComponent';
import { RolesService } from 'src/app/services/roles/roles.service';

@Component({
  selector: 'app-chi-tiet-quyen',
  templateUrl: './chi-tiet-quyen.component.html',
  styleUrls: ['./chi-tiet-quyen.component.scss'],
})
export class ChiTietQuyenComponent extends BaseComponent<Roles> {
  rolesDetail: Roles = new Roles();
  constructor(private rolesService: RolesService, private injector: Injector) {
    super(injector);
  }
  ngOnInit(): void {
    this.onInit();
  }
  override getDetail(id: any) {
    this.rolesService.getDetail(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.rolesDetail = res.data;
          this.rolesDetail.status == '1' &&
            (this.rolesDetail.status = 'Hiệu lực');
          this.rolesDetail.status == '0' &&
            (this.rolesDetail.status = 'Không hiệu lực');
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
}
