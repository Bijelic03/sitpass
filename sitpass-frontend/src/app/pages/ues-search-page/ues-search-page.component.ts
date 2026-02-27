import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { FacilitySearchHitDto, FacilitySearchRequest, FacilityService } from '../../../../api/facility.service';

@Component({
  selector: 'app-ues-search-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ues-search-page.component.html',
  styleUrl: './ues-search-page.component.scss'
})
export class UesSearchPageComponent implements OnInit {
  searchForm: FormGroup;
  results: FacilitySearchHitDto[] = [];
  loading = false;
  error = '';

  constructor(private fb: FormBuilder, private facilityService: FacilityService) {
    this.searchForm = this.fb.group({
      name: [''],
      description: [''],
      pdfDescription: [''],
      minReviews: [''],
      maxReviews: [''],
      minAvgStaff: [''],
      maxAvgStaff: [''],
      minAvgEquipment: [''],
      maxAvgEquipment: [''],
      minAvgHygiene: [''],
      maxAvgHygiene: [''],
      minAvgSpace: [''],
      maxAvgSpace: [''],
      operator: ['AND'],
      sortByNameAsc: [true]
    });
  }

  ngOnInit(): void {
    this.runSearch();
  }

  runSearch(): void {
    this.loading = true;
    this.error = '';
    const request = this.buildRequest();
    this.facilityService.searchFacilities(request).subscribe({
      next: data => {
        this.results = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Search failed.';
        this.loading = false;
      }
    });
  }

  resetFilters(): void {
    this.searchForm.reset({
      name: '',
      description: '',
      pdfDescription: '',
      minReviews: '',
      maxReviews: '',
      minAvgStaff: '',
      maxAvgStaff: '',
      minAvgEquipment: '',
      maxAvgEquipment: '',
      minAvgHygiene: '',
      maxAvgHygiene: '',
      minAvgSpace: '',
      maxAvgSpace: '',
      operator: 'AND',
      sortByNameAsc: true
    });
    this.runSearch();
  }

  runMoreLikeThis(facilityId: number): void {
    const request: FacilitySearchRequest = {
      moreLikeThisFacilityId: facilityId,
      sortByNameAsc: this.searchForm.value.sortByNameAsc
    };
    this.loading = true;
    this.error = '';
    this.facilityService.searchFacilities(request).subscribe({
      next: data => {
        this.results = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'More-like-this search failed.';
        this.loading = false;
      }
    });
  }

  private buildRequest(): FacilitySearchRequest {
    const value = this.searchForm.value;
    return {
      name: value.name || undefined,
      description: value.description || undefined,
      pdfDescription: value.pdfDescription || undefined,
      minReviews: this.numberOrUndefined(value.minReviews),
      maxReviews: this.numberOrUndefined(value.maxReviews),
      minAvgStaff: this.numberOrUndefined(value.minAvgStaff),
      maxAvgStaff: this.numberOrUndefined(value.maxAvgStaff),
      minAvgEquipment: this.numberOrUndefined(value.minAvgEquipment),
      maxAvgEquipment: this.numberOrUndefined(value.maxAvgEquipment),
      minAvgHygiene: this.numberOrUndefined(value.minAvgHygiene),
      maxAvgHygiene: this.numberOrUndefined(value.maxAvgHygiene),
      minAvgSpace: this.numberOrUndefined(value.minAvgSpace),
      maxAvgSpace: this.numberOrUndefined(value.maxAvgSpace),
      operator: value.operator || 'AND',
      sortByNameAsc: !!value.sortByNameAsc
    };
  }

  private numberOrUndefined(value: string | number): number | undefined {
    if (value === '' || value === null || value === undefined) {
      return undefined;
    }
    const numeric = Number(value);
    return Number.isNaN(numeric) ? undefined : numeric;
  }
}
