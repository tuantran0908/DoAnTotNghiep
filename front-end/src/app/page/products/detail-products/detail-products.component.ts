import { Component, Injector } from '@angular/core';
import { BaseComponent } from '../../base/BaseComponent';
import { GiaoTrinh } from 'src/app/model/model';
import { ProductsService } from 'src/app/services/products/products.service';
import { CategoryService } from 'src/app/services/category/category.service';
import { FwError } from 'src/app/common/constants';

@Component({
  selector: 'app-detail-products',
  templateUrl: './detail-products.component.html',
  styleUrls: ['./detail-products.component.scss'],
})
export class DetailProductsComponent extends BaseComponent<GiaoTrinh> {
  giaoTrinhDetail: GiaoTrinh = new GiaoTrinh();
  categorySelected?: any;
  image: any =
    'data:image/png;base64,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';
  constructor(
    private productsService: ProductsService,
    private injector: Injector
  ) {
    super(injector);
  }
  ngOnInit(): void {
    this.onInit();
  }
  override getDetail(id: any) {
    this.productsService.getDetail(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.giaoTrinhDetail = res.data;
          this.giaoTrinhDetail.status == '1' &&
            (this.giaoTrinhDetail.status = 'Hiệu lực');
          this.giaoTrinhDetail.status == '0' &&
            (this.giaoTrinhDetail.status = 'Không hiệu lực');
          this.giaoTrinhDetail.isNew == '1' &&
            (this.giaoTrinhDetail.isNew = 'Giáo trình mới');
          this.giaoTrinhDetail.isNew == '0' &&
            (this.giaoTrinhDetail.isNew = 'Giáo trình cũ');
          if (this.giaoTrinhDetail.category) {
            this.categorySelected = this.giaoTrinhDetail.category;
            this.categorySelected.label =
              this.categorySelected.id +
              (this.categorySelected.name
                ? ' - ' + this.categorySelected.name
                : '');
            this.categorySelected.track = this.categorySelected.id;
          }
        }
      } else {
        this.toastrs.error(res.errorMessage);
      }
    });
  }
}
