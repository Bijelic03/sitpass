import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateFacilityPageComponent } from './create-facility-page.component';
import { FormGroup } from '@angular/forms';

describe('CreateFacilityPageComponent', () => {
  let component: CreateFacilityPageComponent;
  let fixture: ComponentFixture<CreateFacilityPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateFacilityPageComponent, FormGroup]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateFacilityPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
