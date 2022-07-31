import { Component, OnInit } from '@angular/core';
import { Post } from "../../common/post";
import { Router } from "@angular/router";
import { BuyerService } from 'src/app/services/buyer.service';

@Component({
  selector: 'app-interested-buyers',
  templateUrl: './interested-buyers.component.html',
  styleUrls: ['./interested-buyers.component.css']
})
export class InterestedBuyersComponent implements OnInit {

  post: Post;

  constructor(private buyerService: BuyerService, private router: Router) { }

  ngOnInit(): void {
    const userId = localStorage.getItem("user_id");
    if (userId == 'null') {
      this.router.navigateByUrl(`/login`);
    }
    this.listBuyers();
  }

  listBuyers() {

    this.post = history.state.data;
    if (this.post == null) {
      this.router.navigateByUrl(`/user-post`);
    }
  }

  acceptCommunication(buyerId: number) {
    this.buyerService.acceptCommunication(buyerId).subscribe(
      data => {
        if (data.statusText == "OK") {
          for(var buyer of this.post.buyers) {
            if(buyer.id = buyerId) {
              buyer.canCommunicate = true;
              break;
            }
          }
        }
      });

  }

}
