import { Component, Injector } from '@angular/core';
import {
  Category,
  GiaoTrinh,
  GiaoTrinhSearchRequest,
} from 'src/app/model/model';
import { BaseComponent } from '../base/BaseComponent';
import { statusList } from 'src/app/common/utils';
import { ProductsService } from 'src/app/services/products/products.service';
import { MatDialog } from '@angular/material/dialog';
import { CategoryService } from 'src/app/services/category/category.service';
import { FormControl } from '@angular/forms';
import { FwError } from 'src/app/common/constants';
import { ConfirmDialogComponent } from 'src/app/component/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent extends BaseComponent<GiaoTrinh> {
  statusList = statusList;
  giaoTrinhSearch: GiaoTrinhSearchRequest = new GiaoTrinhSearchRequest();
  constructor(
    private productsService: ProductsService,
    private dialog: MatDialog,
    public categoryService: CategoryService,
    private injector: Injector
  ) {
    super(injector);
  }
  searchForm = this.formBuilder.group({
    id: new FormControl(),
    name: new FormControl(),
    description: new FormControl(),
    author: new FormControl(),
    category: new FormControl(),
    status: new FormControl(),
    publicDateFrom: new FormControl(),
    publicDateTo: new FormControl(),
  });
  categorySelected?: Category[];

  ngOnInit(): void {
    this.onInit();
  }

  override search(page: any) {
    this.page.curentPage = page;
    if (this.categorySelected && this.categorySelected.length > 0) {
      this.giaoTrinhSearch.categoryIds = this.categorySelected
        .map((item) => item.id)
        .toString();
    }
    this.productsService
      .searchPaging(this.page, this.giaoTrinhSearch)
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
    this.productsService.delete(id).subscribe((res) => {
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
