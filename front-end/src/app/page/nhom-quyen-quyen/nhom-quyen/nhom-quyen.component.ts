import { Component, Injector } from '@angular/core';
import {
  Roles,
  RolesGroup,
  RolesGroupSearchRequest,
} from 'src/app/model/model';
import { BaseComponent } from '../../base/BaseComponent';
import { statusList } from 'src/app/common/utils';
import { RolesGroupService } from 'src/app/services/roles-group/roles-group.service';
import { FormControl } from '@angular/forms';
import { FwError } from 'src/app/common/constants';
import { RolesService } from 'src/app/services/roles/roles.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../../component/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-nhom-quyen',
  templateUrl: './nhom-quyen.component.html',
  styleUrls: ['./nhom-quyen.component.scss'],
})
export class NhomQuyenComponent extends BaseComponent<RolesGroup> {
  statusList = statusList;
  rolesGroupSearch: RolesGroupSearchRequest = new RolesGroupSearchRequest();
  constructor(
    private rolesGroupService: RolesGroupService,
    private dialog: MatDialog,
    public rolesService: RolesService,
    private injector: Injector
  ) {
    super(injector);
  }
  searchForm = this.formBuilder.group({
    id: new FormControl(),
    description: new FormControl(),
    name: new FormControl(),
    status: new FormControl(),
    roles: new FormControl(),
  });
  rolesSelected?: Roles[];

  ngOnInit(): void {
    this.onInit();
  }

  override search(page: any) {
    this.page.curentPage = page;
    if (this.rolesSelected && this.rolesSelected.length > 0) {
      this.rolesGroupSearch.rolesIds = this.rolesSelected
        .map((item) => item.id)
        .toString();
    }
    this.rolesGroupService
      .searchPaging(this.page, this.rolesGroupSearch)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.totalElements) this.total = res.data.totalElements;
          if (res.data?.content)
            this.asyncData = res.data.content.map((item) => {
              item.status == '1' && (item.status = 'Hiệu lực');
              item.status == '0' && (item.status = 'Không hiệu lực');
              return item;
            });
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }
  delete(id: any) {
    this.rolesGroupService.delete(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        this.toastrs.success('Thành công');
        window.location.reload();
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
  confirmDelete(id: number | undefined, name: String | undefined) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Thông báo',
        name: name,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === true && id !== undefined) {
        this.delete(id);
      }
    });
  }
}
