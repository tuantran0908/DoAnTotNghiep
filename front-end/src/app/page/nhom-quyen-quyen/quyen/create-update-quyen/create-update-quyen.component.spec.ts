import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateQuyenComponent } from './create-update-quyen.component';

describe('CreateUpdateQuyenComponent', () => {
  let component: CreateUpdateQuyenComponent;
  let fixture: ComponentFixture<CreateUpdateQuyenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateUpdateQuyenComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateQuyenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
