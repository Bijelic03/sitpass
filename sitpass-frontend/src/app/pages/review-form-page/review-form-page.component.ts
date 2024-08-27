import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewDto } from '../../../../model/reviewDto';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule, NgIf } from '@angular/common';
import { ReviewService } from '../../../../api/review.service';

@Component({
  selector: 'app-review-form-page',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NgIf],
  templateUrl: './review-form-page.component.html',
  styleUrl: './review-form-page.component.scss'
})
export class ReviewFormPageComponent implements OnInit {
  reviewForm!: FormGroup;
  facilityId!: number;
  userId: number = 0; // Hardcoded user ID
  errorMessage: string = ''; // Error message variable

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private reviewService: ReviewService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const facilityId = this.route.snapshot.paramMap.get('facilityId');
    if (facilityId) {
      this.facilityId = +facilityId;
    } else {
      console.error('Facility ID not found in the URL');
    }

    this.reviewForm = this.fb.group({
      staffRate: [null, [Validators.required, Validators.min(1), Validators.max(10)]],
      equipmentRate: [null, [Validators.required, Validators.min(1), Validators.max(10)]],
      hygieneRate: [null, [Validators.required, Validators.min(1), Validators.max(10)]],
      spaceRate: [null, [Validators.required, Validators.min(1), Validators.max(10)]],
    });
  }

  onSubmit(): void {
    if (this.reviewForm.valid) {
      const review: ReviewDto = {
        authorId: this.userId,
        facilityId: this.facilityId,
        staffRate: this.reviewForm.value.staffRate,
        equipmentRate: this.reviewForm.value.equipmentRate,
        hygieneRate: this.reviewForm.value.hygieneRate,
        spaceRate: this.reviewForm.value.spaceRate,
      };

      this.reviewService.createReview(this.facilityId, review).subscribe(response => {
        this.errorMessage = ''; // Clear any previous error message
        this.router.navigate(['/facilities', this.facilityId]);

        // Handle success response
      }, (error: HttpErrorResponse) => {
        if (error.status === 400) {
          this.errorMessage = 'Bad Request: ' + (error.error.message || 'Invalid request data.');
        } else {
          this.errorMessage = 'An unexpected error occurred. Please try again later.';
        }
        console.error('Review submission failed:', error);
      });
    }
  }
}