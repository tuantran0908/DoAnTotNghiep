import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateProductsComponent } from './create-update-products.component';

describe('CreateUpdateProductsComponent', () => {
  let component: CreateUpdateProductsComponent;
  let fixture: ComponentFixture<CreateUpdateProductsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateUpdateProductsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
