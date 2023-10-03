import { Component, Injector } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { FwError } from 'src/app/common/constants';
import { Roles, RolesGroup, RolesGroupRoles } from 'src/app/model/model';
import { BaseComponent } from 'src/app/page/base/BaseComponent';
import { RolesGroupService } from 'src/app/services/roles-group/roles-group.service';
import { RolesService } from 'src/app/services/roles/roles.service';

@Component({
  selector: 'app-create-update-nhom-quyen',
  templateUrl: './create-update-nhom-quyen.component.html',
  styleUrls: ['./create-update-nhom-quyen.component.scss'],
})
export class CreateUpdateNhomQuyenComponent extends BaseComponent<RolesGroup> {
  rolesGroupDetail: RolesGroup = new RolesGroup();
  rolesList: Roles[] = [];

  constructor(
    private rolesGroupService: RolesGroupService,
    public rolesService: RolesService,
    private injector: Injector
  ) {
    super(injector);
  }
  rolesSelected?: any;
  rolesGroupStatus: boolean = false;
  detailForm = this.formBuilder.group({
    id: new FormControl([Validators.required]),
    description: new FormControl(),
    name: new FormControl([Validators.required]),
    status: new FormControl('', [Validators.required]),
  });

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
              this.rolesGroupStatus = this.rolesGroupDetail.status == '1';
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
  override getInit() {
    this.getRoles(1).subscribe((roles) => {
      if (FwError.THANHCONG == roles.errorCode) {
        if (roles.data?.content) this.rolesList = roles.data.content;
      } else {
        this.toastrs.error(roles.errorMessage);
      }
    });
  }
  override update() {
    this.isSubmit = true;
    if (this.detailForm.status == 'INVALID') {
      this.focusInValidForm();
      return;
    } else if (this.detailForm.status == 'PENDING') {
      this.toastrs.warning('Hệ thống đang xử lý! Vui lòng đợi');
      return;
    } else {
      if (this.rolesGroupStatus) {
        this.rolesGroupDetail.status = '1';
      } else {
        this.rolesGroupDetail.status = '0';
      }
      this.rolesGroupService.update(this.rolesGroupDetail).subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data) {
            this.rolesGroupDetail = res.data;
          }
          this._location.back();
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
    }
  }
  override create() {
    this.isSubmit = true;
    if (this.detailForm.status == 'INVALID') {
      this.focusInValidForm();
      return;
    } else if (this.detailForm.status == 'PENDING') {
      this.toastrs.warning('Hệ thống đang xử lý! Vui lòng đợi');
      return;
    } else {
      if (this.rolesGroupStatus) {
        this.rolesGroupDetail.status = '1';
      } else {
        this.rolesGroupDetail.status = '0';
      }
      this.rolesGroupService.create(this.rolesGroupDetail).subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data) {
            this.rolesGroupDetail = res.data;
          }
          this._location.back();
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
    }
  }
  async onChangeInput(role: Roles) {
    role.value = !role.value;
    let item = this.rolesGroupDetail.rolesGroup_Roles?.find(
      (item) => item.roles!.id == role.id
    );
    if (role.value && item == undefined) {
      let rolesGroup_RolesNew = new RolesGroupRoles();
      rolesGroup_RolesNew.roles = role;
      if (!this.rolesGroupDetail.rolesGroup_Roles) {
        this.rolesGroupDetail.rolesGroup_Roles = [];
      }
      this.rolesGroupDetail.rolesGroup_Roles!.push(rolesGroup_RolesNew);
    } else {
      let index = this.rolesGroupDetail.rolesGroup_Roles!.indexOf(item!);
      index > -1 && this.rolesGroupDetail.rolesGroup_Roles!.splice(index, 1);
    }
  }
}
