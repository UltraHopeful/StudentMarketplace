import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../common/user";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ChangePasswordService {

  private environmentUrl:String = environment.baseUrl;
  private changePasswordUrl = this.environmentUrl + 'api/user/change-password';
  constructor(private httpClient: HttpClient) { }

  updatePassword(userId: number, userMail: string, Old_Password: any, New_Password: any): Observable<any> {
      return this.httpClient.post<User>(this.changePasswordUrl, {userId,userMail,Old_Password,New_Password});
    }
}
