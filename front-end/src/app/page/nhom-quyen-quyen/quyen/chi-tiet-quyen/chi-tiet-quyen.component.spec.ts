import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChiTietQuyenComponent } from './chi-tiet-quyen.component';

describe('ChiTietQuyenComponent', () => {
  let component: ChiTietQuyenComponent;
  let fixture: ComponentFixture<ChiTietQuyenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChiTietQuyenComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChiTietQuyenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
