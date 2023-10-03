import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LayoutFixedComponent } from './layout-fixed.component';

describe('LayoutFixedComponent', () => {
  let component: LayoutFixedComponent;
  let fixture: ComponentFixture<LayoutFixedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LayoutFixedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LayoutFixedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
