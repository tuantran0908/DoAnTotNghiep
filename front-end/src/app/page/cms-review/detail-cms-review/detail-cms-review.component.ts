import { Component, Injector } from '@angular/core';
import { Review } from 'src/app/model/model';
import { BaseComponent } from '../../base/BaseComponent';
import { ReviewService } from 'src/app/services/review/review.service';
import { FwError } from 'src/app/common/constants';

@Component({
  selector: 'app-detail-cms-review',
  templateUrl: './detail-cms-review.component.html',
  styleUrls: ['./detail-cms-review.component.scss'],
})
export class DetailCmsReviewComponent extends BaseComponent<Review> {
  reviewDetail: Review = new Review();
  constructor(
    private reviewService: ReviewService,
    private injector: Injector
  ) {
    super(injector);
  }
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
}
