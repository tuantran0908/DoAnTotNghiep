import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { commons } from 'src/app/common/commons';
import {
  BasePagingResponse,
  BaseResponse,
  Bill,
  GiaoTrinh,
  PagesRequest,
  ReportSearchRequest,
  Review,
} from 'src/app/model/model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private baseUrl = environment.urlServer + '/api/report';
  constructor(
    private http: HttpClient,
    private commons: commons,
    private toastrs: ToastrService
  ) {}

  searchGiaoTrinh(
    page: PagesRequest,
    reportSearchRequest: ReportSearchRequest
  ): Observable<BasePagingResponse<GiaoTrinh>> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    if (page.curentPage) params = params.set('page', page.curentPage - 1);
    if (page.size) params = params.set('size', page.size);
    if (reportSearchRequest.dateFrom) {
      let date = this.commons.toDateUri(reportSearchRequest.dateFrom);
      params = params.set('dateFrom', date!);
    }
    if (reportSearchRequest.dateTo) {
      let date = this.commons.toDateUri(reportSearchRequest.dateTo);
      params = params.set('dateTo', date!);
    }
    return this.http.get<BasePagingResponse<GiaoTrinh>>(
      this.baseUrl + '/giao-trinh',
      {
        params,
      }
    );
  }
  searchReviews(
    page: PagesRequest,
    reportSearchRequest: ReportSearchRequest
  ): Observable<BasePagingResponse<Review>> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    if (page.curentPage) params = params.set('page', page.curentPage - 1);
    if (page.size) params = params.set('size', page.size);
    if (reportSearchRequest.dateFrom) {
      let date = this.commons.toDateUri(reportSearchRequest.dateFrom);
      params = params.set('dateFrom', date!);
    }
    if (reportSearchRequest.dateTo) {
      let date = this.commons.toDateUri(reportSearchRequest.dateTo);
      params = params.set('dateTo', date!);
    }
    return this.http.get<BasePagingResponse<Review>>(
      this.baseUrl + '/reviews',
      {
        params,
      }
    );
  }
  searchBills(
    page: PagesRequest,
    reportSearchRequest: ReportSearchRequest
  ): Observable<BasePagingResponse<Bill>> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    if (page.curentPage) params = params.set('page', page.curentPage - 1);
    if (page.size) params = params.set('size', page.size);
    if (reportSearchRequest.dateFrom) {
      let date = this.commons.toDateUri(reportSearchRequest.dateFrom);
      params = params.set('dateFrom', date!);
    }
    if (reportSearchRequest.dateTo) {
      let date = this.commons.toDateUri(reportSearchRequest.dateTo);
      params = params.set('dateTo', date!);
    }
    return this.http.get<BasePagingResponse<Bill>>(this.baseUrl + '/bills', {
      params,
    });
  }
  getTotalQuantity(): Observable<BaseResponse<any>> {
    return this.http.get<BaseResponse<number>>(this.baseUrl + '/quantity');
  }
  getTotalCategory(): Observable<BaseResponse<any>> {
    return this.http.get<BaseResponse<number>>(this.baseUrl + '/category');
  }
}
