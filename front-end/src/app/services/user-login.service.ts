import { Injectable } from '@angular/core';
import { User } from '../common/user';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserLoginService {
  private environmentUrl:String = environment.baseUrl;
  private userRegistrationUrl = this.environmentUrl + 'api/user/login';

  constructor(private httpClient: HttpClient) { }

  loginUser(user: User): Observable<any> {
    return this.httpClient.post<User>(this.userRegistrationUrl, user,{observe:"response"});
  }
}
