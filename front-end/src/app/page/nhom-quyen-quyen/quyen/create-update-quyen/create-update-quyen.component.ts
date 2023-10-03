import { Component, Injector } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { FwError } from 'src/app/common/constants';
import { Roles } from 'src/app/model/model';
import { BaseComponent } from 'src/app/page/base/BaseComponent';
import { RolesService } from 'src/app/services/roles/roles.service';

@Component({
  selector: 'app-create-update-quyen',
  templateUrl: './create-update-quyen.component.html',
  styleUrls: ['./create-update-quyen.component.scss'],
})
export class CreateUpdateQuyenComponent extends BaseComponent<Roles> {
  rolesDetail: Roles = new Roles();
  constructor(private rolesService: RolesService, private injector: Injector) {
    super(injector);
  }
  rolesStatus: boolean = false;
  detailForm = this.formBuilder.group({
    id: new FormControl([Validators.required]),
    description: new FormControl(),
    name: new FormControl([Validators.required]),
    status: new FormControl('', [Validators.required]),
  });

  ngOnInit(): void {
    this.onInit();
  }
  override getDetail(id: any) {
    this.rolesService.getDetail(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.rolesDetail = res.data;
          this.rolesStatus = this.rolesDetail.status == '1';
        }
      } else {
        this.toastrs.error(res.errorMessage);
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
      if (this.rolesStatus) {
        this.rolesDetail.status = '1';
      } else {
        this.rolesDetail.status = '0';
      }
      this.rolesService.update(this.rolesDetail).subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data) {
            this.rolesDetail = res.data;
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
      if (this.rolesStatus) {
        this.rolesDetail.status = '1';
      } else {
        this.rolesDetail.status = '0';
      }
      this.rolesService.create(this.rolesDetail).subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data) {
            this.rolesDetail = res.data;
          }
          this._location.back();
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
    }
  }
}
