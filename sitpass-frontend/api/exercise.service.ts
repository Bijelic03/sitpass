import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ExerciseDto } from '../model/exerciseDto';

@Injectable({
  providedIn: 'root'
})
export class ExerciseService {
  private apiUrl = 'http://localhost:8080/api/v1/facilities';

  constructor(private http: HttpClient) {}

  createReservation(facilityId: number, exercise: ExerciseDto): Observable<ExerciseDto> {
    return this.http.post<ExerciseDto>(`${this.apiUrl}/${facilityId}/exercises`, exercise);
  }

  getAllFacilityExercises(facilityId: number): Observable<ExerciseDto[]> {
    return this.http.get<ExerciseDto[]>(`${this.apiUrl}/${facilityId}/exercises`);
  }
}
