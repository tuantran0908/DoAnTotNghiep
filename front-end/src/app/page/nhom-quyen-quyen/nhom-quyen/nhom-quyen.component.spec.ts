import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NhomQuyenComponent } from './nhom-quyen.component';

describe('NhomQuyenComponent', () => {
  let component: NhomQuyenComponent;
  let fixture: ComponentFixture<NhomQuyenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NhomQuyenComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NhomQuyenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
