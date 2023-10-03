import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NgselectComponent } from './ngselect.component';

describe('NgselectComponent', () => {
  let component: NgselectComponent;
  let fixture: ComponentFixture<NgselectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NgselectComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NgselectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
