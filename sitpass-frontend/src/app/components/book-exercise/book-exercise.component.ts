import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ExerciseDto } from '../../../../model/exerciseDto';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule, NgIf } from '@angular/common';
import { ExerciseService } from '../../../../api/exercise.service';

@Component({
  selector: 'app-book-exercise',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NgIf],
  templateUrl: './book-exercise.component.html',
  styleUrl: './book-exercise.component.scss'
})
export class BookExerciseComponent implements OnInit {
  bookingForm!: FormGroup;
  facilityId!: number;
  userId: number = 0; // Hardcoded user ID
  errorMessage: string = ''; // Error message variable

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private bookingService: ExerciseService,
    private router: Router

  ) {}

  ngOnInit(): void {
    const facilityId = this.route.snapshot.paramMap.get('facilityId');
    if (facilityId) {
      this.facilityId = +facilityId;
    } else {
      console.error('Facility ID not found in the URL');
    }

    this.bookingForm = this.fb.group({
      fromDate: ['', Validators.required],
      fromTime: ['', Validators.required],
      untilTime: ['', Validators.required]
    }, { validators: this.timeRangeValidator });
  }

  onSubmit(): void {
    if (this.bookingForm.valid) {
      const fromDateTime = this.combineDateTime(this.bookingForm.value.fromDate, this.bookingForm.value.fromTime);
      const untilDateTime = this.combineDateTime(this.bookingForm.value.fromDate, this.bookingForm.value.untilTime);

      const booking: ExerciseDto = {
        fromTime: fromDateTime,
        untilTime: untilDateTime,
        facilityId: this.facilityId,
        userId: this.userId
      };

      this.bookingService.createReservation(this.facilityId, booking).subscribe(response => {
        console.log('Booking successful:', response);
        this.errorMessage = 'Booking successful';
        this.router.navigate(['/facilities', this.facilityId]);

        // Handle success response
      }, (error: HttpErrorResponse) => {
        if (error.status === 400) {
          this.errorMessage = 'Bad Request: ' + (error.error.message || 'Invalid request data.');
        } else {
          this.errorMessage = 'An unexpected error occurred. Please try again later.';
        }
        console.error('Booking failed:', error);
      });
    }
  }

  private combineDateTime(date: string, time: string): string {
    return `${date}T${time}`;
  }

  private timeRangeValidator(control: AbstractControl): ValidationErrors | null {
    const fromTime = control.get('fromTime')?.value;
    const untilTime = control.get('untilTime')?.value;

    if (fromTime && untilTime && fromTime >= untilTime) {
      return { timeRangeInvalid: true };
    }
    return null;
  }
}
