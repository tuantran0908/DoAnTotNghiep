import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { catchError, firstValueFrom, map, Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { FwError } from 'src/app/common/constants';
import { commons } from 'src/app/common/commons';
import {
  BasePagingResponse,
  BaseResponse,
  PageResponse,
  PagesRequest,
  Notification,
} from 'src/app/model/model';

import { BaseService } from '../BaseService';

@Injectable({
  providedIn: 'root'
})
export class NotificationService implements BaseService<Notification> {
  private baseUrl = environment.urlServer + '/api/notification';
  constructor(private http: HttpClient, private toastrs: ToastrService,private commons: commons) {}
  searchSlect2(turn: string | null): Observable<Notification[]> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    params = params.set('page', 0);
    params = params.set('size', 100);
    if (turn) {
      params = params.set('code', turn);
      params.set('name', turn);
    }
    return this.http
      .get<Notification[]>(this.baseUrl, {
        params,
      })
      .pipe(
        map((res: any) => {
          if (FwError.THANHCONG == res.errorCode) {
            return res.data.content.map((item: any) => {
              item.label = item.code + (item.name ? ' - ' + item.name : '');
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
    notification: Notification
  ): Observable<BasePagingResponse<Notification>> {
    let params = new HttpParams();
    params = params.set('sort', 'createdAt,desc');
    if (page.curentPage) params = params.set('page', page.curentPage - 1);
    if (page.size) params = params.set('size', page.size);

    if (notification.name) params = params.set('name', notification.name);
    if (notification.nameEn) params = params.set('nameEn', notification.nameEn);
    if (notification.beginDate){
      let date= this.commons.toDateUri(notification.beginDate)
      if(date)
      params = params.set('beginDate',date );
    }
    if (notification.endDate){
      let date= this.commons.toDateUri(notification.endDate)
      if(date)
      params = params.set('endDate',date );
    }
    return this.http.get<BasePagingResponse<Notification>>(this.baseUrl, {
      params,
    });
  }

  getDetail(code: any): Observable<BaseResponse<Notification>> {
    return this.http.get<BaseResponse<Notification>>(this.baseUrl + '/' + code);
  }

  async keyExist(
    code: any,
  ){
    return firstValueFrom(this.http.get(this.baseUrl+'/keyexist/'+ code));
  }
  
  update(notification: Notification): Observable<BaseResponse<Notification>> {
    return this.http.put<BaseResponse<Notification>>(this.baseUrl, notification);
  }
  create(notification: Notification): Observable<BaseResponse<Notification>> {
    return this.http.post<BaseResponse<Notification>>(this.baseUrl, notification);
  }

  delete(code: any): Observable<BaseResponse<Notification>> {
    return this.http.delete<BaseResponse<Notification>>(this.baseUrl + '/' + code);
  }

}