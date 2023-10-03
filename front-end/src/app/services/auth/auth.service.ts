import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { LoginRequest, LoginResponse } from 'src/app/model/model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private url = environment.urlServer + '/api/auth/signin';
  constructor(private http: HttpClient) {}

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.url, loginRequest).pipe(
      tap((loginResponse) => {
        if (loginResponse) {
          localStorage.setItem('currentUser', JSON.stringify(loginResponse));
        }
      })
    );
  }
  getAuthorization() {
    let user = localStorage.getItem('currentUser');
    if (user) return JSON.parse(user);
  }
}
