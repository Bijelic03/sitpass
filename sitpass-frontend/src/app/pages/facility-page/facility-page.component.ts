import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
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
  uploadMessage = '';
  selectedImages: File[] = [];
  selectedDocument: File | null = null;
  private readonly backendBaseUrl = 'http://localhost:8080';
  private readonly fallbackImage = 'http://localhost:8080/images/gym-1.jpg';

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

  formatCreatedAt(value: any): string {
    if (!value) {
      return '';
    }
    if (Array.isArray(value) && value.length >= 3) {
      const [year, month, day] = value;
      return `${day.toString().padStart(2, '0')}.${month.toString().padStart(2, '0')}.${year}`;
    }
    const parsed = new Date(value);
    if (Number.isNaN(parsed.getTime())) {
      return String(value);
    }
    return parsed.toLocaleDateString('sr-RS');
  }

  getFacilityImages(): string[] {
    const images = this.facility?.images ?? [];
    if (images.length === 0) {
      return [this.fallbackImage];
    }
    return images.map(image => this.resolveImageUrl(image));
  }

  private resolveImageUrl(image: string): string {
    if (!image) {
      return this.fallbackImage;
    }
    if (image.startsWith('http://') || image.startsWith('https://')) {
      return image;
    }
    if (image.startsWith('/')) {
      return `${this.backendBaseUrl}${image}`;
    }
    return `${this.backendBaseUrl}/${image}`;
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

  onImagesSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files) {
      this.selectedImages = [];
      return;
    }
    this.selectedImages = Array.from(input.files);
  }

  onDocumentSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedDocument = input.files && input.files.length > 0 ? input.files[0] : null;
  }

  uploadAssets(): void {
    if (!this.facility?.id) {
      return;
    }
    this.uploadMessage = '';
    this.facilityService.uploadAssets(this.facility.id, this.selectedImages, this.selectedDocument).subscribe({
      next: () => {
        this.uploadMessage = 'Assets uploaded.';
        this.getFacilityDetails(this.facility!.id!);
      },
      error: (error: HttpErrorResponse) => {
        this.uploadMessage = error?.error?.message || 'Upload failed.';
      }
    });
  }

  downloadFacilityPdf(): void {
    if (!this.facility?.id) {
      return;
    }
    window.open(this.facilityService.getDocumentDownloadUrl(this.facility.id), '_blank');
  }

}
