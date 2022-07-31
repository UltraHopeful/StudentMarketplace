import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../common/user';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserRegistrationService {
  private environmentUrl:String = environment.baseUrl;
  private userRegistrationUrl = this.environmentUrl+'api/user/register';

  private verifyUserUrl = this.environmentUrl+'api/user/verify';

  constructor(private httpClient: HttpClient) { }

  registerUser(user: User): Observable<any> {
    return this.httpClient.post<User>(this.userRegistrationUrl, user);
  }

  verifyUser(token: string): Observable<any>{
    // console.log(this.verifyUserUrl+'?token='+token)
    return this.httpClient.get(this.verifyUserUrl+'?token='+token,{observe:"response"});
  }

}
