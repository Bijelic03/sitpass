import { CommonModule, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FacilityService } from '../../../../api/facility.service';

@Component({
  selector: 'app-create-facility-page',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NgIf],
  templateUrl: './create-facility-page.component.html',
  styleUrls: ['./create-facility-page.component.scss']
})
export class CreateFacilityPageComponent implements OnInit {
  facilityForm: FormGroup;
  daysOfWeek = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
  facilityId: number | null = null;  // ID facility-ja, null ako kreiramo novi

  constructor(
    private fb: FormBuilder,
    private facilityService: FacilityService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.facilityForm = this.fb.group({
      id: [''],
      active: [true],
      name: ['', Validators.required],
      description: [''],
      address: ['', Validators.required],
      city: ['', Validators.required],
      createdAt: [new Date().toISOString(), Validators.required],
      workDays: this.fb.array([]),
      disciplines: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.facilityId = +id;
        this.loadFacility(this.facilityId);
      }
    });
  }

  loadFacility(id: number): void {
    this.facilityService.getFacility(id).subscribe(facility => {
      this.facilityForm.patchValue({
        id: facility.id,
        active: facility.active,
        name: facility.name,
        description: facility.description,
        address: facility.address,
        city: facility.city,
        createdAt: facility.createdAt
      });
      if (facility.workDays) {
        this.setWorkDays(facility.workDays);
      }
      if (facility.disciplines) {
        this.setDisciplines(facility.disciplines);
      }
    });
  }

  setWorkDays(workDays: any[]): void {
    const workDaysFormArray = this.facilityForm.get('workDays') as FormArray;
    workDays.forEach(workDay => {
      // Konvertujemo nizove sati i minuta u string HH:mm
      const fromTime = `${workDay.fromTime[0].toString().padStart(2, '0')}:${workDay.fromTime[1].toString().padStart(2, '0')}`;
      const untilTime = `${workDay.untilTime[0].toString().padStart(2, '0')}:${workDay.untilTime[1].toString().padStart(2, '0')}`;
  
      // Pravimo novi JavaScript Date objekat za validFrom
      const validFromDate = new Date(workDay.validFrom[0], workDay.validFrom[1] - 1, workDay.validFrom[2]);
  
      workDaysFormArray.push(this.fb.group({
        id: [workDay.id],
        day: [workDay.day, Validators.required],
        fromTime: [fromTime, Validators.required],
        untilTime: [untilTime, Validators.required],
        validFrom: [validFromDate.toISOString().split('T')[0], Validators.required] // ISO format za datum
      }));
    });
  }

  setDisciplines(disciplines: any[]): void {
    const disciplinesFormArray = this.facilityForm.get('disciplines') as FormArray;
    disciplines.forEach(discipline => {
      disciplinesFormArray.push(this.fb.group({
        id: [discipline.id],
        name: [discipline.name, Validators.required]
      }));
    });
  }

  get workDays(): FormArray {
    return this.facilityForm.get('workDays') as FormArray;
  }

  addWorkDay(): void {
    if (this.workDays.length < 7) {
      this.workDays.push(this.fb.group({
        day: ['', Validators.required],
        fromTime: ['', Validators.required],
        untilTime: ['', Validators.required],
        validFrom: ['', Validators.required]
      }));
    }
  }

  removeWorkDay(index: number): void {
    this.workDays.removeAt(index);
  }

  getAvailableDays(index: number): string[] {
    const selectedDays = this.workDays.controls
    .map((control, idx) => (idx !== index ? control.get('day')?.value : null)) 
    .filter(day => day !== null) as string[];
      return this.daysOfWeek.filter(day => !selectedDays.includes(day));
  }

  getSelectedDay(index: number): string | null {
    const dayControl = this.workDays.at(index)?.get('day');
    return dayControl ? dayControl.value : null;
  }

  get disciplines(): FormArray {
    return this.facilityForm.get('disciplines') as FormArray;
  }

  addDiscipline(): void {
    this.disciplines.push(this.fb.group({
      name: ['', Validators.required]
    }));
  }

  removeDiscipline(index: number): void {
    this.disciplines.removeAt(index);
  }

  onSubmit(): void {
    if (this.facilityId) {
      // Update existing facility
      this.facilityService.updateFacility(this.facilityId, this.facilityForm.value).subscribe((data) => {
        console.log('Facility updated:', data);
        this.router.navigate(['/']);
      });
    } else {
      // Create new facility
      this.facilityService.createFacility(this.facilityForm.value).subscribe((data) => {
        console.log('Facility created:', data);
        this.router.navigate(['/']);
      });
    }
  }

  formatTime(timeString: any): string {
    if (typeof timeString === 'string') {
      const formattedTime = timeString.replace(',', '-');
      return formattedTime;
    }
    return ''; 
  }
}
