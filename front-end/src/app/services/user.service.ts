import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../common/user';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";
import {Post} from "../common/post";
import {map} from "rxjs/operators";
import { UserPostRequest } from '../common/user-post-request';

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private environmentUrl: String = environment.baseUrl;
    private userPostsUrl = this.environmentUrl + 'api/user-posts';
    private baseUserProfileUrl = this.environmentUrl + 'api/user/profile-details';

    private buyersUrl = this.environmentUrl + 'api/posts';

    private deleteUrl = this.environmentUrl + 'api/user/delete-profile';

    constructor(private httpClient: HttpClient) { }

    getUser(theUserId: number): Observable<User> {

        // need to build URL based on user id
        const userUrl = `${this.baseUserProfileUrl}/${theUserId}`;

        return this.httpClient.get<User>(userUrl);
    }

    userPostsPaginate(userPostRequest: UserPostRequest): Observable<GetResponsePosts> {

        return this.httpClient.post<GetResponsePosts>(this.userPostsUrl, userPostRequest);
      }

    getBuyerList(thePostId: number): Observable<User[]> {

        // need to build URL based on post id
        var searchUrl = this.buyersUrl;

        if (thePostId > 0) {
            searchUrl = `${this.buyersUrl}/${thePostId}/buyers`;
        }
        return this.getInterestedBuyers(searchUrl);
    }

    getInterestedBuyers(searchUrl: string): Observable<User[]> {
        return this.httpClient.get<GetResponseUsers>(searchUrl).pipe(map(response => response._embedded.buyers));
    }

    deleteUserProfile(user: User): Observable<any> {
        return this.httpClient.post<User>(this.deleteUrl, user);
    }
}

interface GetResponsePosts {
    content: Post[],
    totalElements: number,
    size: number,
    totalPages: number,
    number: number
  }

interface GetResponseUsers {
    _embedded: {
        buyers: User[];
    }
}
