import { Component, Input } from '@angular/core';
import { FacilityDto } from '../../../../model/facilityDto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-facility-card',
  standalone: true,
  imports: [],
  templateUrl: './facility-card.component.html',
  styleUrl: './facility-card.component.scss'
})
export class FacilityCardComponent {
  @Input() facility: FacilityDto = {} as FacilityDto;

  constructor(private router: Router) {}


  navigateToDetails() {
    this.router.navigate(['/facilities', this.facility.id]);
  }
}
