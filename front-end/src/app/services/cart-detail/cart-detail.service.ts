import { Injectable } from '@angular/core';
import { BaseService } from '../BaseService';
import {
  BasePagingResponse,
  BaseResponse,
  CartDetail,
  CartDetailSearchRequest,
  NgParam,
  PagesRequest,
} from 'src/app/model/model';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CartDetailService implements BaseService<CartDetail> {
  private baseUrl = environment.urlServer + '/api/cart-detail';
  constructor(private http: HttpClient, private toastrs: ToastrService) {}
  searchSlect2(turn: string | null, customparam: NgParam[]) {
    throw new Error('Method not implemented.');
  }

  searchPaging(
    page: PagesRequest,
    cartDetail: CartDetailSearchRequest
  ): Observable<BasePagingResponse<CartDetail>> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    if (page.curentPage) params = params.set('page', page.curentPage - 1);
    if (page.size) params = params.set('size', page.size);
    if (cartDetail.userId) params = params.set('userId', cartDetail.userId);
    return this.http.get<BasePagingResponse<CartDetail>>(this.baseUrl, {
      params,
    });
  }
  getDetail(id: any): Observable<BaseResponse<CartDetail>> {
    return this.http.get<BaseResponse<CartDetail>>(this.baseUrl + '/' + id);
  }
  getCount(id: any): Observable<BaseResponse<number>> {
    return this.http.get<BaseResponse<number>>(this.baseUrl + '/count/' + id);
  }
  update(cartDetail: CartDetail): Observable<BaseResponse<CartDetail>> {
    return this.http.put<BaseResponse<CartDetail>>(this.baseUrl, cartDetail);
  }
  create(cartDetail: CartDetail): Observable<BaseResponse<CartDetail>> {
    return this.http.post<BaseResponse<CartDetail>>(this.baseUrl, cartDetail);
  }
  delete(id: any): Observable<BaseResponse<CartDetail>> {
    return this.http.delete<BaseResponse<CartDetail>>(this.baseUrl + '/' + id);
  }
}
