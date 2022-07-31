import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Post} from 'src/app/common/post';
import {PostService} from 'src/app/services/post.service';

@Component({
  selector: 'app-post-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.css']
})
export class PostDetailsComponent implements OnInit {

  post: Post = new Post();

  constructor(private postService: PostService,
              private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    const userId: number = +localStorage.getItem("user_id");
    if (userId < 1) {
      this.router.navigateByUrl(`/login`);
    }

    this.post = history.state.data;
    var from: string = history.state.from;
    if (this.post == null) {
      this.router.navigateByUrl(`/posts`);
    } else if (from != "InterestedPost") {
      for (var buyer of this.post.buyers) {
        if (buyer.user.id == userId && buyer.canCommunicate) {
          this.post.acceptedCommunication = true;
          break;
        }
      }
    }
  }
}
