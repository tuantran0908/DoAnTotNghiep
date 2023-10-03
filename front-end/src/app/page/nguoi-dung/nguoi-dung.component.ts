import { Component, Injector } from '@angular/core';
import { FormControl } from '@angular/forms';
import { statusList } from 'src/app/common/utils';
import {
  NgParam,
  RolesGroup,
  User,
  UserSearchRequest,
} from 'src/app/model/model';
import { BaseComponent } from '../base/BaseComponent';
import { FwError } from 'src/app/common/constants';
import { UserService } from 'src/app/services/user/user.service';
import { RolesGroupService } from 'src/app/services/roles-group/roles-group.service';
import { ConfirmDialogComponent } from 'src/app/component/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-nguoi-dung',
  templateUrl: './nguoi-dung.component.html',
  styleUrls: ['./nguoi-dung.component.scss'],
})
export class NguoiDungComponent extends BaseComponent<User> {
  statusList = statusList;
  userSearch: UserSearchRequest = new UserSearchRequest();
  constructor(
    private userService: UserService,
    private dialog: MatDialog,
    public rolesGroupService: RolesGroupService,
    private injector: Injector
  ) {
    super(injector);
  }
  searchForm = this.formBuilder.group({
    name: new FormControl(),
    status: new FormControl(),
    rolesGroup: new FormControl(),
    createdAtFrom: new FormControl(),
    createdAtTo: new FormControl(),
  });
  rolesGroupSelected?: RolesGroup[];

  ngOnInit(): void {
    this.onInit();
  }

  override search(page: any) {
    this.page.curentPage = page;
    if (this.rolesGroupSelected && this.rolesGroupSelected.length > 0) {
      this.userSearch.rolesGroupIds = this.rolesGroupSelected
        .map((item) => item.id)
        .toString();
    }
    this.userService
      .searchPaging(this.page, this.userSearch)
      .subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data?.totalElements) this.total = res.data.totalElements;
          if (res.data?.content)
            this.asyncData = res.data.content.map((item) => {
              item.status == '1' && (item.status = 'Hiệu lực');
              item.status == '0' && (item.status = 'Không hiệu lực');
              item.gender == '1' && (item.gender = 'Male');
              item.gender == '2' && (item.gender = 'Female');
              return item;
            });
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
  }
  delete(id: any) {
    this.userService.delete(id).subscribe((res) => {
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
