import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChinhSuaNguoiDungComponent } from './chinh-sua-nguoi-dung.component';

describe('ChinhSuaNguoiDungComponent', () => {
  let component: ChinhSuaNguoiDungComponent;
  let fixture: ComponentFixture<ChinhSuaNguoiDungComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChinhSuaNguoiDungComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChinhSuaNguoiDungComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
