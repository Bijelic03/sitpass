import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookExerciseComponent } from './book-exercise.component';

describe('BookExerciseComponent', () => {
  let component: BookExerciseComponent;
  let fixture: ComponentFixture<BookExerciseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookExerciseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookExerciseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
