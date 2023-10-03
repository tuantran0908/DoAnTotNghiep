import { Injectable } from '@angular/core';
import { BaseService } from '../BaseService';
import {
  BasePagingResponse,
  BaseResponse,
  NgParam,
  PagesRequest,
  Roles,
} from 'src/app/model/model';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { Observable, catchError, map, of } from 'rxjs';
import { FwError } from 'src/app/common/constants';

@Injectable({
  providedIn: 'root',
})
export class RolesService implements BaseService<Roles> {
  private baseUrl = environment.urlServer + '/api/roles';
  constructor(private http: HttpClient, private toastrs: ToastrService) {}
  searchSlect2(
    turn: string | null,
    customparam: NgParam[]
  ): Observable<Roles[]> {
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
      .get<Roles[]>(this.baseUrl + '/search-select2', {
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

  searchSlect(turn: string | null): Observable<Roles[]> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    params = params.set('page', 0);
    params = params.set('size', 10);

    if (turn) {
      params = params.set('id', turn);
    }

    return this.http
      .get<Roles[]>(this.baseUrl, {
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
    roles: Roles
  ): Observable<BasePagingResponse<Roles>> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    if (page.curentPage) params = params.set('page', page.curentPage - 1);
    if (page.size) params = params.set('size', page.size);
    if (roles.status) params = params.set('status', roles.status);
    if (roles.name) params = params.set('name', roles.name);
    if (roles.description)
      params = params.set('description', roles.description);
    return this.http.get<BasePagingResponse<Roles>>(this.baseUrl, {
      params,
    });
  }
  getDetail(id: any): Observable<BaseResponse<Roles>> {
    return this.http.get<BaseResponse<Roles>>(this.baseUrl + '/' + id);
  }
  update(roles: Roles): Observable<BaseResponse<Roles>> {
    return this.http.put<BaseResponse<Roles>>(this.baseUrl, roles);
  }
  create(roles: Roles): Observable<BaseResponse<Roles>> {
    return this.http.post<BaseResponse<Roles>>(this.baseUrl, roles);
  }
  delete(id: any): Observable<BaseResponse<Roles>> {
    return this.http.delete<BaseResponse<Roles>>(this.baseUrl + '/' + id);
  }
}
