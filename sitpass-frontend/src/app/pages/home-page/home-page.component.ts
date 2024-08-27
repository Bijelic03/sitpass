import { Component, OnInit } from '@angular/core';
import { FacilityCardComponent } from '../../components/facility-card/facility-card.component';
import { FacilityDto } from '../../../../model/facilityDto';
import { NgForOf, NgIf } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { LocalTime } from '../../../../model/localTime'; // Import LocalTime if necessary
import { FacilityService } from '../../../../api/facility.service';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [FacilityCardComponent, NgForOf, NgIf, ReactiveFormsModule],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.scss'
})
export class HomePageComponent implements OnInit {
  facilities: FacilityDto[] = [];
  filteredFacilities: FacilityDto[] = [];
  searchForm: FormGroup;

  constructor(private facilityService: FacilityService, private fb: FormBuilder) {
    this.searchForm = this.fb.group({
      city: [''],
      discipline: [''],
      minRating: [''],
      maxRating: [''],
      workDay: [''],
      fromTime: [''],
      untilTime: ['']
    });
  }

  ngOnInit(): void {
    this.getFacilities();
  }

  getFacilities(): void {
    this.facilityService.getFacilities().subscribe((data) => {
      this.facilities = data.filter(facility => facility.active);
      this.filteredFacilities = [...this.facilities];
    });
  }

  onSearch(): void {
    const { city, discipline, minRating, maxRating, workDay, fromTime, untilTime } = this.searchForm.value;
    this.filteredFacilities = this.facilities.filter(facility => {
      const matchesCity = city ? facility.city?.toLowerCase().includes(city.toLowerCase()) : true;
      const matchesDiscipline = discipline ? facility.disciplines?.some(d => d.name?.toLowerCase().includes(discipline.toLowerCase())) : true;
      const matchesMinRating = minRating ? facility.totalRating && facility.totalRating >= minRating : true;
      const matchesMaxRating = maxRating ? facility.totalRating && facility.totalRating <= maxRating : true;
      const matchesWorkDay = workDay ? facility.workDays?.some(wd => wd.day?.toLowerCase() === workDay.toLowerCase()) : true;
      const matchesFromTime = fromTime ? facility.workDays?.some(wd => wd.fromTime && this.compareLocalTime(wd.fromTime, fromTime) <= 0) : true;
      const matchesUntilTime = untilTime ? facility.workDays?.some(wd => wd.untilTime && this.compareLocalTime(wd.untilTime, untilTime) >= 0) : true;
      console.log("search")
      return matchesCity && matchesDiscipline && matchesMinRating && matchesMaxRating && matchesWorkDay && matchesFromTime && matchesUntilTime;
    });
  }

  compareLocalTime(time1: LocalTime, time2: string): number {
    const [hour2, minute2] = time2.split(':').map(Number);
    const hour1 = time1.hour;
    const minute1 = time1.minute;
    if(hour1 && minute1){
    if (hour1 < hour2 || (hour1 === hour2 && minute1 < minute2)) {
      return -1;
    }
    if (hour1 > hour2 || (hour1 === hour2 && minute1 > minute2)) {
      return 1;
    }
  }
    return 0;
  }
}
