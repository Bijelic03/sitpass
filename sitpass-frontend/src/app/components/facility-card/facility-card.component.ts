import { Component, Input } from '@angular/core';
import { FacilityDto } from '../../../../model/facilityDto';
import { Router } from '@angular/router';
import { FacilityService } from '../../../../api/facility.service';

@Component({
  selector: 'app-facility-card',
  standalone: true,
  imports: [],
  templateUrl: './facility-card.component.html',
  styleUrl: './facility-card.component.scss'
})
export class FacilityCardComponent {
  @Input() facility: FacilityDto = {} as FacilityDto;
  private readonly backendBaseUrl = 'http://localhost:8080';
  private readonly fallbackImage = 'http://localhost:8080/images/gym-1.jpg';

  constructor(private router: Router, private facilityService: FacilityService) {}

  getPrimaryImage(): string {
    const image = this.facility.images?.[0];
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

  navigateToDetails() {
    this.router.navigate(['/facilities', this.facility.id]);
  }

  downloadPdf(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    if (!this.facility.id) {
      return;
    }
    window.open(this.facilityService.getDocumentDownloadUrl(this.facility.id), '_blank');
  }
}
