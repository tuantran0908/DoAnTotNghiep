import { Component, Injector } from '@angular/core';
import { map } from 'rxjs';
import { FwError } from 'src/app/common/constants';
import { Roles, RolesGroup } from 'src/app/model/model';
import { BaseComponent } from 'src/app/page/base/BaseComponent';
import { RolesGroupService } from 'src/app/services/roles-group/roles-group.service';
import { RolesService } from 'src/app/services/roles/roles.service';

@Component({
  selector: 'app-chi-tiet-nhom-quyen',
  templateUrl: './chi-tiet-nhom-quyen.component.html',
  styleUrls: ['./chi-tiet-nhom-quyen.component.scss'],
})
export class ChiTietNhomQuyenComponent extends BaseComponent<RolesGroup> {
  rolesGroupDetail: RolesGroup = new RolesGroup();
  rolesList: Roles[] = [];
  constructor(
    private rolesGroupService: RolesGroupService,
    private rolesService: RolesService,
    private injector: Injector
  ) {
    super(injector);
  }
  ngOnInit(): void {
    this.onInit();
  }
  getRoles(page: any) {
    this.page.curentPage = page;
    this.page.size = 100;
    return this.rolesService.searchPaging(this.page, new Roles());
  }
  override getDetail(id: any) {
    this.getRoles(1).subscribe((roles) => {
      if (FwError.THANHCONG == roles.errorCode) {
        if (roles.data?.content) this.rolesList = roles.data.content;
        this.rolesGroupService.getDetail(id).subscribe((rolesGroup) => {
          if (FwError.THANHCONG == rolesGroup.errorCode) {
            if (rolesGroup.data) {
              this.rolesGroupDetail = rolesGroup.data;
              this.rolesGroupDetail.status == '1' &&
                (this.rolesGroupDetail.status = 'Hiệu lực');
              this.rolesGroupDetail.status == '0' &&
                (this.rolesGroupDetail.status = 'Không hiệu lực');
              this.rolesGroupDetail.rolesGroup_Roles?.forEach(
                (rolesGroupRole) => {
                  this.rolesList.find((roles) => {
                    if (roles.id == rolesGroupRole.roles!.id) {
                      roles.value = true;
                    }
                  });
                }
              );
            }
          } else {
            this.toastrs.error(rolesGroup.errorMessage);
          }
        });
      } else {
        this.toastrs.error(roles.errorMessage);
      }
    });
  }
}
