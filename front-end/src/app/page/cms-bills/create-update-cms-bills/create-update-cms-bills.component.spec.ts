import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateCmsBillsComponent } from './create-update-cms-bills.component';

describe('CreateUpdateCmsBillsComponent', () => {
  let component: CreateUpdateCmsBillsComponent;
  let fixture: ComponentFixture<CreateUpdateCmsBillsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateUpdateCmsBillsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateCmsBillsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
