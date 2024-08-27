import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FacilityDto } from '../../../../model/facilityDto';
import { ReviewDto } from '../../../../model/reviewDto';
import { CommonModule, NgIf } from '@angular/common';
import { DatePipe } from '@angular/common';
import { FacilityService } from '../../../../api/facility.service';
import { ReviewService } from '../../../../api/review.service';

@Component({
  selector: 'app-facility-page',
  standalone: true,
  imports: [CommonModule, NgIf],
  templateUrl: './facility-page.component.html',
  styleUrls: ['./facility-page.component.scss'],
  providers: [DatePipe],
})
export class FacilityPageComponent implements OnInit {
  facility: FacilityDto | undefined;
  reviews: ReviewDto[] = [];
  showModal = false;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private datePipe: DatePipe,
    private facilityService: FacilityService,
    private reviewService: ReviewService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.getFacilityDetails(+id);
      this.getReviews(+id);
    }
  }

  getFacilityDetails(id: number): void {
    this.http.get<FacilityDto>(`http://localhost:8080/api/v1/facilities/${id}`).subscribe(
      (data: FacilityDto) => {
        this.facility = data;
      },
      (error) => {
        console.error('Error fetching facility details:', error);
      }
    );
  }

  getReviews(facilityId: number): void {
    this.reviewService.getAllReviews(facilityId).subscribe(
      (data: ReviewDto[]) => {
        this.reviews = data;
      },
      (error) => {
        console.error('Error fetching reviews:', error);
      }
    );
  }

  formatTime(time: any | undefined): string {
    if (!time || time.length < 2) return '';
    const [hour, minute] = time;
    return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
  }

  deleteFacility() {
    if (this.facility?.id) {
      this.facility.active = false;
      this.facilityService.updateFacility(this.facility.id, this.facility).subscribe((data) => {
        this.router.navigate(['/']);
      });
    }
  }

  editFacility() {
    this.router.navigate(['/facilities', 'edit', this.facility?.id]);
  }

  reserveFacility() {
    this.router.navigate(['/book-exercise', this.facility?.id]);
  }

  leaveReview() {
    this.router.navigate(['/leave-review', this.facility?.id]);
  }

}
