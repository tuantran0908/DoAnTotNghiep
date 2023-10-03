import { Component, Injector } from '@angular/core';
import { BaseComponent } from '../../base/BaseComponent';
import { Category } from 'src/app/model/model';
import { CategoryService } from 'src/app/services/category/category.service';
import { FormControl, Validators } from '@angular/forms';
import { FwError } from 'src/app/common/constants';

@Component({
  selector: 'app-create-update-category',
  templateUrl: './create-update-category.component.html',
  styleUrls: ['./create-update-category.component.scss'],
})
export class CreateUpdateCategoryComponent extends BaseComponent<Category> {
  categoryDetail: Category = new Category();
  constructor(
    private categoryService: CategoryService,
    private injector: Injector
  ) {
    super(injector);
  }
  categoryStatus: boolean = false;
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
    this.categoryService.getDetail(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.categoryDetail = res.data;
          this.categoryStatus = this.categoryDetail.status == '1';
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
      if (this.categoryStatus) {
        this.categoryDetail.status = '1';
      } else {
        this.categoryDetail.status = '0';
      }
      this.categoryService.update(this.categoryDetail).subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data) {
            this.categoryDetail = res.data;
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
      if (this.categoryStatus) {
        this.categoryDetail.status = '1';
      } else {
        this.categoryDetail.status = '0';
      }
      this.categoryService.create(this.categoryDetail).subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data) {
            this.categoryDetail = res.data;
          }
          this._location.back();
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
    }
  }
}
