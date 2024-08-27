import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FacilityDto } from '../model/facilityDto';

@Injectable({
  providedIn: 'root'
})
export class FacilityService {

  private baseUrl = 'http://localhost:8080/api/v1/facilities';

  constructor(private http: HttpClient) { }

  getFacilities(): Observable<FacilityDto[]> {
    return this.http.get<FacilityDto[]>(this.baseUrl);
  }

  updateFacility(id: number, facilityDto: FacilityDto): Observable<FacilityDto> {
    if (id === null || id === undefined) {
      throw new Error('Required parameter id was null or undefined when calling updateFacility.');
    }
    if (facilityDto === null || facilityDto === undefined) {
      throw new Error('Required parameter facilityDto was null or undefined when calling updateFacility.');
    }

    const url = `${this.baseUrl}/${id}`;
    const headers = new HttpHeaders().set('Content-Type', 'application/json');

    return this.http.put<FacilityDto>(url, facilityDto, { headers: headers });
  }

  getFacility(id: number): Observable<FacilityDto> {
    if (id === null || id === undefined) {
      throw new Error('Required parameter id was null or undefined when calling getFacility.');
    }
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<FacilityDto>(url);
  }

  createFacility(facility: FacilityDto): Observable<FacilityDto> {
    return this.http.post<FacilityDto>(this.baseUrl, facility);
  }
}
