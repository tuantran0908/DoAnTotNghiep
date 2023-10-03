import { Injectable } from '@angular/core';
import { BaseService } from '../BaseService';
import {
  BasePagingResponse,
  BaseResponse,
  BaseResponseList,
  Category,
  NgParam,
  PagesRequest,
} from 'src/app/model/model';
import { environment } from 'src/environments/environment';
import { ToastrService } from 'ngx-toastr';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, map, of } from 'rxjs';
import { FwError } from 'src/app/common/constants';

@Injectable({
  providedIn: 'root',
})
export class CategoryService implements BaseService<Category> {
  private baseUrl = environment.urlServer + '/api/category';
  constructor(private http: HttpClient, private toastrs: ToastrService) {}
  searchSlect2(
    turn: string | null,
    customparam: NgParam[]
  ): Observable<Category[]> {
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
      .get<Category[]>(this.baseUrl + '/search-select2', {
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

  searchSlect(turn: string | null): Observable<Category[]> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    params = params.set('page', 0);
    params = params.set('size', 10);

    if (turn) {
      params = params.set('id', turn);
    }

    return this.http
      .get<Category[]>(this.baseUrl, {
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
    category: Category
  ): Observable<BasePagingResponse<Category>> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    if (page.curentPage) params = params.set('page', page.curentPage - 1);
    if (page.size) params = params.set('size', page.size);
    if (category.status) params = params.set('status', category.status);
    if (category.name) params = params.set('name', category.name);
    if (category.description)
      params = params.set('description', category.description);
    return this.http.get<BasePagingResponse<Category>>(this.baseUrl, {
      params,
    });
  }
  getDetail(id: any): Observable<BaseResponse<Category>> {
    return this.http.get<BaseResponse<Category>>(this.baseUrl + '/' + id);
  }
  getAllCategory(): Observable<BaseResponseList<Category>> {
    return this.http.get<BaseResponseList<Category>>(this.baseUrl + '/all');
  }
  update(category: Category): Observable<BaseResponse<Category>> {
    return this.http.put<BaseResponse<Category>>(this.baseUrl, category);
  }
  create(category: Category): Observable<BaseResponse<Category>> {
    return this.http.post<BaseResponse<Category>>(this.baseUrl, category);
  }
  delete(id: any): Observable<BaseResponse<Category>> {
    return this.http.delete<BaseResponse<Category>>(this.baseUrl + '/' + id);
  }
}
