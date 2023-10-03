import { Component, Injector } from '@angular/core';
import { BaseComponent } from '../../base/BaseComponent';
import { Category } from 'src/app/model/model';
import { CategoryService } from 'src/app/services/category/category.service';
import { FwError } from 'src/app/common/constants';

@Component({
  selector: 'app-detail-category',
  templateUrl: './detail-category.component.html',
  styleUrls: ['./detail-category.component.scss'],
})
export class DetailCategoryComponent extends BaseComponent<Category> {
  categoryDetail: Category = new Category();
  constructor(
    private categoryService: CategoryService,
    private injector: Injector
  ) {
    super(injector);
  }
  ngOnInit(): void {
    this.onInit();
  }
  override getDetail(id: any) {
    this.categoryService.getDetail(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.categoryDetail = res.data;
          this.categoryDetail.status == '1' &&
            (this.categoryDetail.status = 'Hiệu lực');
          this.categoryDetail.status == '0' &&
            (this.categoryDetail.status = 'Không hiệu lực');
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
}
