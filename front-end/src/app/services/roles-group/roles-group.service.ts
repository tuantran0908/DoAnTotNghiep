import { Injectable } from '@angular/core';
import { BaseService } from '../BaseService';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import {
  BasePagingResponse,
  BaseResponse,
  NgParam,
  PagesRequest,
  RolesGroup,
  RolesGroupSearchRequest,
} from 'src/app/model/model';
import { Observable, catchError, map, of } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { FwError } from 'src/app/common/constants';

@Injectable({
  providedIn: 'root',
})
export class RolesGroupService implements BaseService<RolesGroup> {
  private baseUrl = environment.urlServer + '/api/roles-group';
  constructor(private http: HttpClient, private toastrs: ToastrService) {}
  searchSlect2(
    turn: string | null,
    customparam: NgParam[]
  ): Observable<RolesGroup[]> {
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
      .get<RolesGroup[]>(this.baseUrl + '/search-select2', {
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

  searchSlect(turn: string | null): Observable<RolesGroup[]> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    params = params.set('page', 0);
    params = params.set('size', 10);

    if (turn) {
      params = params.set('id', turn);
    }

    return this.http
      .get<RolesGroup[]>(this.baseUrl, {
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
    rolesGroup: RolesGroupSearchRequest
  ): Observable<BasePagingResponse<RolesGroup>> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    if (page.curentPage) params = params.set('page', page.curentPage - 1);
    if (page.size) params = params.set('size', page.size);
    if (rolesGroup.status) params = params.set('status', rolesGroup.status);
    if (rolesGroup.name) params = params.set('name', rolesGroup.name);
    if (rolesGroup.description)
      params = params.set('description', rolesGroup.description);
    if (rolesGroup.rolesIds)
      params = params.set('rolesIds', rolesGroup.rolesIds);
    return this.http.get<BasePagingResponse<RolesGroup>>(this.baseUrl, {
      params,
    });
  }
  getDetail(id: any): Observable<BaseResponse<RolesGroup>> {
    return this.http.get<BaseResponse<RolesGroup>>(this.baseUrl + '/' + id);
  }
  update(rolesGroup: RolesGroup): Observable<BaseResponse<RolesGroup>> {
    return this.http.put<BaseResponse<RolesGroup>>(this.baseUrl, rolesGroup);
  }
  create(rolesGroup: RolesGroup): Observable<BaseResponse<RolesGroup>> {
    return this.http.post<BaseResponse<RolesGroup>>(this.baseUrl, rolesGroup);
  }
  delete(id: any): Observable<BaseResponse<RolesGroup>> {
    return this.http.delete<BaseResponse<RolesGroup>>(this.baseUrl + '/' + id);
  }
}
