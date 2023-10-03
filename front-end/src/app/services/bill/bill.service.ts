import { Injectable } from '@angular/core';
import {
  BasePagingResponse,
  BaseResponse,
  Bill,
  BillSearchRequest,
  NgParam,
  PagesRequest,
} from 'src/app/model/model';
import { environment } from 'src/environments/environment';
import { BaseService } from '../BaseService';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BillService implements BaseService<Bill> {
  private baseUrl = environment.urlServer + '/api/bill';
  constructor(private http: HttpClient, private toastrs: ToastrService) {}
  searchSlect2(turn: string | null, customparam: NgParam[]) {
    throw new Error('Method not implemented.');
  }

  searchPaging(
    page: PagesRequest,
    bill: BillSearchRequest
  ): Observable<BasePagingResponse<Bill>> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    if (page.curentPage) params = params.set('page', page.curentPage - 1);
    if (page.size) params = params.set('size', page.size);
    if (bill.name) params = params.set('name', bill.name);
    if (bill.phoneNumber) params = params.set('phoneNumber', bill.phoneNumber);
    if (bill.status) params = params.set('status', bill.status);
    if (bill.payment) params = params.set('payment', bill.payment);
    return this.http.get<BasePagingResponse<Bill>>(this.baseUrl, {
      params,
    });
  }
  getDetail(id: any): Observable<BaseResponse<Bill>> {
    return this.http.get<BaseResponse<Bill>>(this.baseUrl + '/' + id);
  }
  getCount(id: any): Observable<BaseResponse<number>> {
    return this.http.get<BaseResponse<number>>(this.baseUrl + '/count/' + id);
  }
  update(bill: Bill): Observable<BaseResponse<Bill>> {
    return this.http.put<BaseResponse<Bill>>(this.baseUrl, bill);
  }
  create(bill: Bill): Observable<BaseResponse<Bill>> {
    return this.http.post<BaseResponse<Bill>>(this.baseUrl, bill);
  }
  delete(id: any): Observable<BaseResponse<Bill>> {
    return this.http.delete<BaseResponse<Bill>>(this.baseUrl + '/' + id);
  }
}
