import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacilityPageComponent } from './facility-page.component';
import { ReserveFacilityModalComponent } from '../../components/reserve-facility-modal/reserve-facility-modal.component';

describe('FacilityPageComponent', () => {
  let component: FacilityPageComponent;
  let fixture: ComponentFixture<FacilityPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FacilityPageComponent, ReserveFacilityModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FacilityPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
