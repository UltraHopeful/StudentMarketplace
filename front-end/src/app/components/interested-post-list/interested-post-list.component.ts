import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Buyer } from 'src/app/common/buyer';
import { Post } from 'src/app/common/post';
import { User } from 'src/app/common/user';
import { BuyerService } from 'src/app/services/buyer.service';

@Component({
  selector: 'app-interested-post-list',
  templateUrl: './interested-post-list.component.html',
  styleUrls: ['./interested-post-list.component.css']
})
export class InterestedPostListComponent implements OnInit {

  buyers: Buyer[] = [];


  constructor(private buyerService: BuyerService, private router: Router) { }

  ngOnInit(): void {
    const userId = localStorage.getItem("user_id");
    if (userId == 'null') {
      this.router.navigateByUrl(`/login`);
    }
    this.listPosts();
  }

  listPosts() {

    let user = new User();
    user.id = +localStorage.getItem("user_id");

    // console.log(JSON.stringify(user));
    // now search for the posts using keyword
    this.buyerService.getInterestedList(user).subscribe(this.processResult());
  }

  processResult() {
    return data => {
      this.buyers = data;
    };
  }

  goToPostDetail(post: Post, canCommunicate: boolean) {
    post.acceptedCommunication = canCommunicate;
    this.router.navigate(['/post-details'], { state: { data: post , from: "InterestedPost"} });
  }
}
