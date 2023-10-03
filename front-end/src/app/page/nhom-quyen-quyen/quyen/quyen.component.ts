import { Component, Injector } from '@angular/core';
import { BaseComponent } from '../../base/BaseComponent';
import { Roles } from 'src/app/model/model';
import { statusList } from 'src/app/common/utils';
import { RolesService } from 'src/app/services/roles/roles.service';
import { FormControl } from '@angular/forms';
import { FwError } from 'src/app/common/constants';
import { ConfirmDialogComponent } from 'src/app/component/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-quyen',
  templateUrl: './quyen.component.html',
  styleUrls: ['./quyen.component.scss'],
})
export class QuyenComponent extends BaseComponent<Roles> {
  statusList = statusList;
  rolesSearch: Roles = new Roles();
  constructor(private rolesService: RolesService, private dialog: MatDialog,private injector: Injector) {
    super(injector);
  }
  searchForm = this.formBuilder.group({
    id: new FormControl(),
    description: new FormControl(),
    name: new FormControl(),
    status: new FormControl(),
    rolesIds: new FormControl(),
  });

  ngOnInit(): void {
    this.onInit();
  }

  override search(page: any) {
    this.page.curentPage = page;
    this.rolesService
      .searchPaging(this.page, this.rolesSearch)
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
    this.rolesService.delete(id).subscribe((res) => {
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
