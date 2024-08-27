import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewFormPageComponent } from './review-form-page.component';

describe('ReviewFormPageComponent', () => {
  let component: ReviewFormPageComponent;
  let fixture: ComponentFixture<ReviewFormPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewFormPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewFormPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
