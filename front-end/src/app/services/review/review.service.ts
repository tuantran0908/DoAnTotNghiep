import { Injectable } from '@angular/core';
import { BaseService } from '../BaseService';
import {
  BasePagingResponse,
  BaseResponse,
  BillSearchRequest,
  NgParam,
  PagesRequest,
  Review,
  ReviewSearchRequest,
} from 'src/app/model/model';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReviewService implements BaseService<Review> {
  private baseUrl = environment.urlServer + '/api/review';
  constructor(private http: HttpClient, private toastrs: ToastrService) {}
  searchSlect2(turn: string | null, customparam: NgParam[]) {
    throw new Error('Method not implemented.');
  }

  searchPaging(
    page: PagesRequest,
    review: ReviewSearchRequest
  ): Observable<BasePagingResponse<Review>> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    if (page.curentPage) params = params.set('page', page.curentPage - 1);
    if (page.size) params = params.set('size', page.size);
    if (review.star) params = params.set('star', review.star);
    if (review.message) params = params.set('message', review.message);
    if (review.userId) params = params.set('userId', review.userId);
    if (review.giaoTrinhId)
      params = params.set('giaoTrinhId', review.giaoTrinhId);
    return this.http.get<BasePagingResponse<Review>>(this.baseUrl, {
      params,
    });
  }
  getDetail(id: any): Observable<BaseResponse<Review>> {
    return this.http.get<BaseResponse<Review>>(this.baseUrl + '/' + id);
  }
  update(review: Review): Observable<BaseResponse<Review>> {
    return this.http.put<BaseResponse<Review>>(this.baseUrl, review);
  }
  create(review: Review): Observable<BaseResponse<Review>> {
    return this.http.post<BaseResponse<Review>>(this.baseUrl, review);
  }
  delete(id: any): Observable<BaseResponse<Review>> {
    return this.http.delete<BaseResponse<Review>>(this.baseUrl + '/' + id);
  }
}
