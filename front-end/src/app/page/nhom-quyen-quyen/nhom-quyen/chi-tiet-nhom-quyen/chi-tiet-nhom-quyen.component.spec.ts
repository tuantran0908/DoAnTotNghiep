import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChiTietNhomQuyenComponent } from './chi-tiet-nhom-quyen.component';

describe('ChiTietNhomQuyenComponent', () => {
  let component: ChiTietNhomQuyenComponent;
  let fixture: ComponentFixture<ChiTietNhomQuyenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChiTietNhomQuyenComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChiTietNhomQuyenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
