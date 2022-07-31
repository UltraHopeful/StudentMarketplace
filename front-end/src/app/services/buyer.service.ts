import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Buyer } from '../common/buyer';
import { BuyerRequest } from '../common/buyer-request';
import { User } from '../common/user';

@Injectable({
  providedIn: 'root'
})
export class BuyerService {

  private environmentUrl: String = environment.baseUrl;

  private markAsInterestedUrl = this.environmentUrl + 'api/buyer/mark-as-interested';
  private acceptCommunicationUrl = this.environmentUrl + 'api/buyer/accept-communication';
  private getInterestedListUrl = this.environmentUrl + 'api/buyer/get-interested-list';

  constructor(private httpClient: HttpClient) { }

  markAsInterested(buyerRequest: BuyerRequest): Observable<any> {
    return this.httpClient.post<GetResponseMessage>(this.markAsInterestedUrl, buyerRequest, { observe: "response" });
  }

  acceptCommunication(buyerId: number): Observable<any> {
    return this.httpClient.post<GetResponseMessage>(this.acceptCommunicationUrl, buyerId, { observe: "response" });
  }

  getInterestedList(user: User): Observable<GetResponseInterestedList> {
    return this.httpClient.post<GetResponseInterestedList>(this.getInterestedListUrl, user);
  }
}

interface GetResponseMessage {
  _embedded: {
    message: string;
  }
}

interface GetResponseInterestedList {
  content: Buyer[]
}