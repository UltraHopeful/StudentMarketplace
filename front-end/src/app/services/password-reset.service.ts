import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../common/user";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PasswordResetService {

  private environmentUrl:String = environment.baseUrl;

  private forgotPasswordUrl = this.environmentUrl + 'api/user/forgot-password';
  private confirmResetUrl = this.environmentUrl + 'api/user/confirm-reset';
  private resetPasswordUrl = this.environmentUrl + 'api/user/reset-password';


  constructor(private httpClient: HttpClient) { }

  forgotPassword(email : string): Observable<any> {
    return this.httpClient.get<User>(this.forgotPasswordUrl+"?emailId="+email);
  }
  checkResetPasswordLink(token: string): Observable<any> {
    return this.httpClient.get<User>(this.confirmResetUrl+"?token="+token,{observe:"response"});
  }
  updatePassword(user: User,token: string): Observable<any> {
    return this.httpClient.post<User>(this.resetPasswordUrl+"?token="+token, user);
  }
}
