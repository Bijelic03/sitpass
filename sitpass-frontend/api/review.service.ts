import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReviewDto } from '../model/reviewDto';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = 'http://localhost:8080/api/v1/facilities';

  constructor(private http: HttpClient) {}

  createReview(facilityId: number, review: ReviewDto): Observable<ReviewDto> {
    return this.http.post<ReviewDto>(`${this.apiUrl}/${facilityId}/reviews`, review);
  }

  getAllReviews(facilityId: number): Observable<ReviewDto[]> {
    return this.http.get<ReviewDto[]>(`${this.apiUrl}/${facilityId}/reviews`);
  }
}
