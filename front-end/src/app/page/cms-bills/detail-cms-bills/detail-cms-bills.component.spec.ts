import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailCmsBillsComponent } from './detail-cms-bills.component';

describe('DetailCmsBillsComponent', () => {
  let component: DetailCmsBillsComponent;
  let fixture: ComponentFixture<DetailCmsBillsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailCmsBillsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailCmsBillsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
