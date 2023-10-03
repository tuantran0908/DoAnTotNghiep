import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import {
  BasePagingResponse,
  BaseResponse,
  GiaoTrinh,
  GiaoTrinhSearchRequest,
  NgParam,
  PagesRequest,
} from 'src/app/model/model';
import { BaseService } from '../BaseService';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable, catchError, map, of } from 'rxjs';
import { FwError, StatusEnum } from 'src/app/common/constants';
import { commons } from 'src/app/common/commons';
@Injectable({
  providedIn: 'root',
})
export class ProductsService implements BaseService<GiaoTrinh> {
  private baseUrl = environment.urlServer + '/api/giao-trinh';
  constructor(
    private http: HttpClient,
    private toastrs: ToastrService,
    private commons: commons
  ) {}
  searchSlect2(
    turn: string | null,
    customparam: NgParam[]
  ): Observable<GiaoTrinh[]> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    params = params.set('page', 0);
    params = params.set('size', 10);

    if (turn) {
      params = params.set('id', turn);
    }
    if (customparam) {
      customparam.forEach((item) => {
        if (item && item.key && item.value) {
          params = params.set(item.key, item.value);
        }
      });
    }
    return this.http
      .get<GiaoTrinh[]>(this.baseUrl + '/search-select2', {
        params,
      })
      .pipe(
        map((res: any) => {
          if (FwError.THANHCONG == res.errorCode) {
            return res.data.content.map((item: any) => {
              item.label = item.id + (item.name ? ' - ' + item.name : '');
              item.track = item.id;
              return item;
            });
          } else {
            this.toastrs.error(res.errorMessage);
          }
        }),
        catchError(() => of([]))
      );
  }

  searchSlect(turn: string | null): Observable<GiaoTrinh[]> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    params = params.set('page', 0);
    params = params.set('size', 10);

    if (turn) {
      params = params.set('id', turn);
    }

    return this.http
      .get<GiaoTrinh[]>(this.baseUrl, {
        params,
      })
      .pipe(
        map((res: any) => {
          if (FwError.THANHCONG == res.errorCode) {
            return res.data.content.map((item: any) => {
              item.label = item.id + (item.name ? ' - ' + item.name : '');
              item.track = item.id;
              return item;
            });
          } else {
            this.toastrs.error(res.errorMessage);
          }
        }),
        catchError(() => of([]))
      );
  }
  searchPaging(
    page: PagesRequest,
    giaoTrinh: GiaoTrinhSearchRequest
  ): Observable<BasePagingResponse<GiaoTrinh>> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    if (page.curentPage) params = params.set('page', page.curentPage - 1);
    if (page.size) params = params.set('size', page.size);
    if (giaoTrinh.status) params = params.set('status', giaoTrinh.status);
    if (giaoTrinh.name) params = params.set('name', giaoTrinh.name);
    if (giaoTrinh.author) params = params.set('author', giaoTrinh.author);
    if (giaoTrinh.description)
      params = params.set('description', giaoTrinh.description);
    if (giaoTrinh.categoryIds)
      params = params.set('categoryIds', giaoTrinh.categoryIds);
    if (giaoTrinh.publicDateFrom) {
      let date = this.commons.toDateUri(giaoTrinh.publicDateFrom);
      params = params.set('publicDateFrom', date!);
    }
    if (giaoTrinh.publicDateTo) {
      let date = this.commons.toDateUri(giaoTrinh.publicDateTo);
      params = params.set('publicDateTo', date!);
    }
    if (giaoTrinh.isNew) params = params.set('isNew', giaoTrinh.isNew);
    if (giaoTrinh.saleProduct)
      params = params.set('saleProduct', giaoTrinh.saleProduct);
    if (giaoTrinh.lastestProduct) {
      params = params.set('sort', 'publicDate,desc');
    }
    if (giaoTrinh.bestSeller) {
      params = params.set('sort', 'quantitySell,desc');
    }
    if (giaoTrinh.favoriteProduct)
      params = params.set('favoriteProduct', giaoTrinh.favoriteProduct);
    if (giaoTrinh.author) params = params.set('author', giaoTrinh.author);
    if (giaoTrinh.priceFrom)
      params = params.set('priceFrom', giaoTrinh.priceFrom);
    if (giaoTrinh.priceTo) params = params.set('priceTo', giaoTrinh.priceTo);
    if (giaoTrinh.star) params = params.set('star', giaoTrinh.star);
    return this.http.get<BasePagingResponse<GiaoTrinh>>(this.baseUrl, {
      params,
    });
  }
  getDetail(id: any): Observable<BaseResponse<GiaoTrinh>> {
    return this.http.get<BaseResponse<GiaoTrinh>>(this.baseUrl + '/' + id);
  }
  update(
    giaoTrinh: GiaoTrinh,
    image: any
  ): Observable<BaseResponse<GiaoTrinh>> {
    var formData = new FormData();
    if (image.length > 0) {
      for (const row of image) {
        formData.append('file', row);
      }
    }
    formData.append(
      'data',
      new Blob([JSON.stringify(giaoTrinh)], {
        type: 'application/json',
      })
    );
    return this.http.put<BaseResponse<GiaoTrinh>>(this.baseUrl, formData);
  }
  create(
    giaoTrinh: GiaoTrinh,
    image: any
  ): Observable<BaseResponse<GiaoTrinh>> {
    giaoTrinh.publicDate = new Date();
    var formData = new FormData();
    if (image.length > 0) {
      for (const row of image) {
        formData.append('file', row);
      }
    }
    formData.append(
      'data',
      new Blob([JSON.stringify(giaoTrinh)], {
        type: 'application/json',
      })
    );
    return this.http.post<BaseResponse<GiaoTrinh>>(this.baseUrl, formData);
  }
  delete(id: any): Observable<BaseResponse<GiaoTrinh>> {
    return this.http.delete<BaseResponse<GiaoTrinh>>(this.baseUrl + '/' + id);
  }
}
