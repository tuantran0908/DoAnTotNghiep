import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateNhomQuyenComponent } from './create-update-nhom-quyen.component';

describe('CreateUpdateNhomQuyenComponent', () => {
  let component: CreateUpdateNhomQuyenComponent;
  let fixture: ComponentFixture<CreateUpdateNhomQuyenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateUpdateNhomQuyenComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateNhomQuyenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
