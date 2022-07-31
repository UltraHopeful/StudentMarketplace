import {
  Injectable
} from '@angular/core';
import {
  HttpClient
} from '@angular/common/http';
import {
  Observable
} from 'rxjs';
import {
  User
} from '../common/user';
import {environment} from "../../environments/environment";


@Injectable({
  providedIn: 'root'
})
export class EditProfileService {

  private environmentUrl:String = environment.baseUrl;
  private editProfileUrl = this.environmentUrl+'api/user/profile-details';

  private userUpdateUrl = this.environmentUrl+'api/user/edit-profile';

  constructor(private httpClient: HttpClient) {}

  getUser(theUserId: number): Observable < User > {

    // need to build URL based on user id
    const userUrl = `${this.editProfileUrl}/${theUserId}`;

    return this.httpClient.get < User > (userUrl);
  }

  updateUser(user: User): Observable<any> {
    return this.httpClient.post<User>(this.userUpdateUrl, user);
  }

}
