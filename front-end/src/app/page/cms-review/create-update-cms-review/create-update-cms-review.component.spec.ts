import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateCmsReviewComponent } from './create-update-cms-review.component';

describe('CreateUpdateCmsReviewComponent', () => {
  let component: CreateUpdateCmsReviewComponent;
  let fixture: ComponentFixture<CreateUpdateCmsReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateUpdateCmsReviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateCmsReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
