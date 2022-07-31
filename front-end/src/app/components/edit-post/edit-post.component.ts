import {Component, OnInit} from '@angular/core';
import {FormBuilder,FormControl, FormGroup, Validators} from "@angular/forms";
import {Post} from "../../common/post";
import {User} from "../../common/user";
import {Media} from "../../common/media";
import {CustomValidators} from "../../validators/custom-validators";
import {BuyerService} from "../../services/buyer.service";
import {Router} from "@angular/router";
import {Category} from "../../common/category";
import {PostService} from "../../services/post.service";
import {PostDetail} from "../../common/post-detail";
import {UpdatePost} from "../../common/update-post";

@Component({
    selector: 'app-edit-post',
    templateUrl: './edit-post.component.html',
    styleUrls: ['./edit-post.component.css']
})
export class EditPostComponent implements OnInit {

    post:Post;
    categories: Category[] = [];

    categoryMode: string = "";
    editPostFormGroup: FormGroup;
    downloadUrl: string;

    constructor(private formBuilder: FormBuilder,private postService: PostService, private router: Router) {
    }

    ngOnInit(): void {
        const userId = localStorage.getItem("user_id");
        // console.log("Hello");
        if (userId == 'null') {
            this.router. navigateByUrl(`/login`);
        }
        this.post = history.state.data;
        // console.log("Hello");
        // console.log(JSON.stringify(this.post));
        // console.log(this.post.title);
        // console.log(this.post.category.categoryName);
        // console.log(this.post.id);
        // console.log(this.post.negotiable);

        this.editPostFormGroup = this.formBuilder.group({
            editPost: this.formBuilder.group({
                title: new FormControl('',
                    [Validators.required, Validators.minLength(2), CustomValidators.notOnlyWhitespace]),
                description: new FormControl('',
                    [Validators.required, Validators.minLength(2), CustomValidators.notOnlyWhitespace]),
                price: new FormControl('',
                    [Validators.required]),
                category: new FormControl('',
                    [Validators.required]),
                negotiable: new FormControl('',
                    [Validators.required]),
                media: new FormControl('')
                // [Validators.required])
            })
        });

        // populate categories
        this.postService.getPostCategories().subscribe(
            data => {
                this.categories = data;
            }
        );

    }

    onSubmit() {
        // console.log("Handling the submit button");

        if (this.editPostFormGroup.invalid) {
            // console.log("Invalid Form");
            this.editPostFormGroup.markAllAsTouched();
            return;
        }

        let post = this.post;

        // TODO: Temporary, till we start saving user info in sesssion

        let user = new User();
        user.id = +localStorage.getItem("user_id");
        user.email = localStorage.getItem("user_email");

        let updatePost = new UpdatePost();
        updatePost.post = post;

        // console.log(updatePost);

        // Get Post Details
        // const postDetails = this.getCategoryDetails();
        // postDetails.forEach((postDetail) => {
        //     console.log("Post Detail => Field: " + postDetail.fieldName + " | Value: " + postDetail.fieldValue);
        // });
        // console.log("Post Details: " + postDetails.toString);

        // updatePost.postDetails = postDetails;

        // Create Media - Doing for only one image now
        let media: Media[] = [];
        var imageDetail = new Media();
        imageDetail.location = this.downloadUrl;
        media.push(imageDetail);

        // updatePost.media = media;

        // call REST API via the Post Service
        this.postService.updatePost(updatePost).subscribe({
            next: response => {
                alert(`Post Updated: ${response.message}`);

                // reset form
                this.resetForm();

            },
            error: err => {
                alert(`There was an error: ${err.message}`);
            }
        });
    }

    resetForm() {
        // reset the form
        this.editPostFormGroup.reset();

        // navigate to the login page
        this.router.navigateByUrl("/user-post");
    }


    // Post Fields
    get title() { return this.editPostFormGroup.get('editPost.title'); }
    get description() { return this.editPostFormGroup.get('editPost.description'); }
    get price() { return this.editPostFormGroup.get('editPost.price'); }
    get category() { return this.editPostFormGroup.get('editPost.category'); }
    get negotiable() { return this.editPostFormGroup.get('editPost.negotiable'); }
    get media() { return this.editPostFormGroup.get('editPost.media'); }

}
