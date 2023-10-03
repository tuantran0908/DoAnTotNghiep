import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CmsReviewComponent } from './cms-review.component';

describe('CmsReviewComponent', () => {
  let component: CmsReviewComponent;
  let fixture: ComponentFixture<CmsReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CmsReviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CmsReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
