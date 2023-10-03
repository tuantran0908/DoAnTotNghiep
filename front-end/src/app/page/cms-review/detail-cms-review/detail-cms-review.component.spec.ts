import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailCmsReviewComponent } from './detail-cms-review.component';

describe('DetailCmsReviewComponent', () => {
  let component: DetailCmsReviewComponent;
  let fixture: ComponentFixture<DetailCmsReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailCmsReviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailCmsReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
