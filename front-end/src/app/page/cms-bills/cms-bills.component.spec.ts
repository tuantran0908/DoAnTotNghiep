import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CmsBillsComponent } from './cms-bills.component';

describe('CmsBillsComponent', () => {
  let component: CmsBillsComponent;
  let fixture: ComponentFixture<CmsBillsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CmsBillsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CmsBillsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
