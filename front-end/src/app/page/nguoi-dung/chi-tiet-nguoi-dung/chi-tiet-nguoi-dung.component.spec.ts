import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChiTietNguoiDungComponent } from './chi-tiet-nguoi-dung.component';

describe('ChiTietNguoiDungComponent', () => {
  let component: ChiTietNguoiDungComponent;
  let fixture: ComponentFixture<ChiTietNguoiDungComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChiTietNguoiDungComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChiTietNguoiDungComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
