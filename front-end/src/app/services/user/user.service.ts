import { Injectable } from '@angular/core';
import { BaseService } from '../BaseService';
import { environment } from 'src/environments/environment';
import {
  BasePagingResponse,
  BaseResponse,
  PagesRequest,
  User,
  UserSearchRequest,
} from 'src/app/model/model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { formatDate } from '@angular/common';
import { commons } from 'src/app/common/commons';

@Injectable({
  providedIn: 'root',
})
export class UserService implements BaseService<User> {
  private baseUrl = environment.urlServer + '/api/user';
  constructor(
    private http: HttpClient,
    private toastrs: ToastrService,
    private commons: commons
  ) {}
  searchSlect2(turn: string | null): Observable<User[]> {
    throw new Error('Method not implemented.');
  }
  searchPaging(
    page: PagesRequest,
    user: UserSearchRequest
  ): Observable<BasePagingResponse<User>> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    if (page.curentPage) params = params.set('page', page.curentPage - 1);
    if (page.size) params = params.set('size', page.size);
    if (user.email) params = params.set('email', user.email);
    if (user.name) params = params.set('name', user.name);
    if (user.status) params = params.set('status', user.status);
    if (user.rolesGroupIds)
      params = params.set('rolesGroupIds', user.rolesGroupIds);
    if (user.createdAtFrom) {
      let date = this.commons.toDateUri(user.createdAtFrom);
      params = params.set('createdAtFrom', date!);
    }
    if (user.createdAtTo) {
      let date = this.commons.toDateUri(user.createdAtTo);
      params = params.set('createdAtTo', date!);
    }
    return this.http.get<BasePagingResponse<User>>(this.baseUrl, {
      params,
    });
  }
  getDetail(id: any): Observable<BaseResponse<User>> {
    return this.http.get<BaseResponse<User>>(this.baseUrl + '/' + id);
  }
  update(user: User, avatar: any): Observable<BaseResponse<User>> {
    var formData = new FormData();
    if (avatar.length > 0) {
      for (const row of avatar) {
        formData.append('file', row);
      }
    }
    formData.append(
      'data',
      new Blob([JSON.stringify(user)], {
        type: 'application/json',
      })
    );
    return this.http.put<BaseResponse<User>>(this.baseUrl, formData);
  }
  create(user: User, avatar: any): Observable<BaseResponse<User>> {
    var formData = new FormData();
    if (avatar.length > 0) {
      for (const row of avatar) {
        formData.append('file', row);
      }
    }
    formData.append(
      'data',
      new Blob([JSON.stringify(user)], {
        type: 'application/json',
      })
    );
    return this.http.post<BaseResponse<User>>(this.baseUrl, formData);
  }
  delete(id: any): Observable<BaseResponse<User>> {
    return this.http.delete<BaseResponse<User>>(this.baseUrl + '/' + id);
  }
}
