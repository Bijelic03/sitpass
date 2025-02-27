import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacilityCardComponent } from './facility-card.component';

describe('FacilityCardComponent', () => {
  let component: FacilityCardComponent;
  let fixture: ComponentFixture<FacilityCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FacilityCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FacilityCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
