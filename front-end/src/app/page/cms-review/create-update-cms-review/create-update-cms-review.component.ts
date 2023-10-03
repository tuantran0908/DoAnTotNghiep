import { Component, Injector } from '@angular/core';
import { Review } from 'src/app/model/model';
import { BaseComponent } from '../../base/BaseComponent';
import { ReviewService } from 'src/app/services/review/review.service';
import { FormControl, Validators } from '@angular/forms';
import { FwError } from 'src/app/common/constants';

@Component({
  selector: 'app-create-update-cms-review',
  templateUrl: './create-update-cms-review.component.html',
  styleUrls: ['./create-update-cms-review.component.scss'],
})
export class CreateUpdateCmsReviewComponent extends BaseComponent<Review> {
  reviewDetail: Review = new Review();

  constructor(
    private reviewService: ReviewService,
    private injector: Injector
  ) {
    super(injector);
  }
  reviewStatus: boolean = false;
  detailForm = this.formBuilder.group({
    id: new FormControl([Validators.required]),
    fullname: new FormControl([Validators.required]),
    message: new FormControl([Validators.required]),
  });

  ngOnInit(): void {
    this.onInit();
  }
  override getDetail(id: any) {
    this.reviewService.getDetail(id).subscribe((res) => {
      if (FwError.THANHCONG == res.errorCode) {
        if (res.data) {
          this.reviewDetail = res.data;
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
      this.reviewService.update(this.reviewDetail).subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data) {
            this.reviewDetail = res.data;
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
      this.reviewService.create(this.reviewDetail).subscribe((res) => {
        if (FwError.THANHCONG == res.errorCode) {
          if (res.data) {
            this.reviewDetail = res.data;
          }
          this._location.back();
        } else {
          this.toastrs.error(res.errorMessage);
        }
      });
    }
  }
  onChecked(value: number) {
    this.reviewDetail.star = value;
  }
}
