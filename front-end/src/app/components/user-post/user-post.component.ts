import { Component, OnInit } from '@angular/core';
import { PostService } from "../../services/post.service";
import { ActivatedRoute, Router } from "@angular/router";
import { UserService } from "../../services/user.service";
import { Category } from "../../common/category";
import { Post } from "../../common/post";
import { User } from 'src/app/common/user';
import { UserPostRequest } from 'src/app/common/user-post-request';
import { NgbActiveModal, NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import {AddPost} from "../../common/add-post";
import {Media} from "../../common/media";

@Component({
    selector: 'app-user-post',
    templateUrl: './user-post.component.html',
    styleUrls: ['./user-post.component.css']
})
export class UserPostComponent implements OnInit {

    posts: Post[] = [];
    user: User = new User();
    postToDelete = new Post();

    // new properties for pagination
    thePageNumber: number = 1;
    thePageSize: number = 6;
    theTotalElements: number = 0;

    constructor(private userService: UserService, private postService: PostService, private modalService: NgbModal, private router: Router) {
    }

    ngOnInit(): void {

        const userId = localStorage.getItem("user_id");
        if (userId == 'null') {
            this.router.navigateByUrl(`\login`);
        }
        this.user.id = +userId;
        this.user.email = '' + localStorage.getItem("user_email");
        this.listPosts();
    }

    closeResult = '';

    listPosts() {
        var userPostRequest = new UserPostRequest();
        userPostRequest.user = this.user;
        userPostRequest.pageNumber = this.thePageNumber - 1;
        userPostRequest.pageSize = this.thePageSize;

        // now search for the posts using keyword
        this.userService.userPostsPaginate(userPostRequest).subscribe(this.processResult());
    }

    processResult() {
        return data => {
            this.posts = data.content;
            this.thePageNumber = data.number + 1;
            this.thePageSize = data.size;
            this.theTotalElements = data.totalElements;
        };
    }

    updatePageSize(pageSize: number) {
        this.thePageSize = pageSize;
        this.thePageNumber = 1;
        this.listPosts();
    }

    newChange(): void {
        this.router.navigateByUrl('interested-buyers');
    }

    editPost(post:Post): void {
        // console.log(JSON.stringify(post));
        this.router.navigate(['/edit-post'],{ state: { data: post } });
    }

    onPostSelected(post: Post) {
        this.router.navigate(['/post-buyers'], { state: { data: post } });
    }

    markAsSold(postId: number) {
        this.postService.markAsSold(postId).subscribe(
            data => {
                if (data.statusText == "OK") {
                    // console.log("Marked as Sold!");

                    for(var eachPost of this.posts) {
                        if(eachPost.id == postId) {
                            eachPost.status = 'Sold';
                            break;
                        }
                    }
                    this.posts = this.posts.filter(post => post.id != this.postToDelete.id);
                }
                this.postToDelete = new Post();
            });

    }

    open(content, post: Post) {
        this.postToDelete = post;
        this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
            this.closeResult = `Closed with: ${result}`;
        }, (reason) => {
            this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
        });
    }

    onDelete() {
        this.postService.deletePost(this.postToDelete.id).subscribe(
            data => {
                if (data.statusText == "OK") {
                    // console.log("Post Deleted");
                    this.posts = this.posts.filter(post => post.id != this.postToDelete.id);
                }
                this.postToDelete = new Post();
            });

            this.modalService.dismissAll();
    }


    private getDismissReason(reason: any): string {
        if (reason === ModalDismissReasons.ESC) {
            return 'by pressing ESC';
        } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
            return 'by clicking on a backdrop';
        } else {
            return `with: ${reason}`;
        }
    }
}
