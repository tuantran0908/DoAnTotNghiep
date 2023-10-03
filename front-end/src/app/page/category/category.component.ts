import { Component, Injector } from '@angular/core';
import { BaseComponent } from '../base/BaseComponent';
import { Category, Roles } from 'src/app/model/model';
import { statusList } from 'src/app/common/utils';
import { CategoryService } from 'src/app/services/category/category.service';
import { FormControl } from '@angular/forms';
import { FwError } from 'src/app/common/constants';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/component/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss'],
})
export class CategoryComponent extends BaseComponent<Category> {
  statusList = statusList;
  categorySearch: Category = new Category();
  constructor(
    private categoryService: CategoryService,
    private dialog: MatDialog,
    private injector: Injector
  ) {
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
    this.categoryService
      .searchPaging(this.page, this.categorySearch)
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
    this.categoryService.delete(id).subscribe((res) => {
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
