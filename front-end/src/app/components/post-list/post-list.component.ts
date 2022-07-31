import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { openStdin } from 'process';
import { BuyerRequest } from 'src/app/common/buyer-request';
import { Category } from 'src/app/common/category';
import { CategoryPost } from 'src/app/common/category-post';
import { Post } from 'src/app/common/post';
import { PostDetail } from 'src/app/common/post-detail';
import { SearchRequest } from 'src/app/common/search-request';
import { User } from 'src/app/common/user';
import { BuyerService } from 'src/app/services/buyer.service';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.css']
})
export class PostListComponent implements OnInit {

  posts: Post[] = [];
  currentCategoryId: number = 1;
  previousCategoryId: number = 1;
  searchMode: boolean = false;
  categoryMode: boolean = false;
  currentUserId: number = -1;

  // new properties for pagination
  thePageNumber: number = 1;
  thePageSize: number = 6;
  theTotalElements: number = 0;

  previousKeyword: string = null;

  constructor(private postService: PostService, private buyerService: BuyerService, private route: ActivatedRoute, private router: Router, private toastrService: ToastrService) { }

  ngOnInit() {
    this.currentUserId = +localStorage.getItem("user_id");
    if (this.currentUserId < 1) {
      this.router.navigateByUrl(`\login`);
    }
    this.route.paramMap.subscribe(() => {
      this.listPosts();
    });

  }

  listPosts() {

    this.searchMode = this.route.snapshot.paramMap.has('keyword');
    this.categoryMode = this.route.snapshot.paramMap.has('categoryId');

    if (this.searchMode) {
      this.handleSearchPosts();
    } else if (this.categoryMode) {
      this.handleCategoryPosts();
    } else {
      this.searchMode = false;
      this.categoryMode = false;
      this.handleAllPosts();
    }
  }

  handleSearchPosts() {
    var keyword: string = this.route.snapshot.paramMap.get('keyword');
    var sortBy: string = this.route.snapshot.paramMap.get('sortBy') || "0";
    var priceMin: string = this.route.snapshot.paramMap.get('priceMin');
    var priceMax: string = this.route.snapshot.paramMap.get('priceMax');
    var postDetails: PostDetail[] = [];

    if (keyword == "-1") {
      keyword = "";
    }

    // Prepare condition PostDetail list
    var excellent: string = this.route.snapshot.paramMap.get('excellent');
    var lightUsage: string = this.route.snapshot.paramMap.get('lightUsage');
    // console.log('lightUsage: ' + lightUsage);
    var visibleUsage: string = this.route.snapshot.paramMap.get('visibleUsage');
    // console.log('Visible: ' + visibleUsage);
    var heavyUsage: string = this.route.snapshot.paramMap.get('heavyUsage');

    var postDetails: PostDetail[] = [];

    // Get Conditional Details
    var conditionDetails: PostDetail[] = this.getConditionList(excellent, lightUsage, visibleUsage, heavyUsage);
    conditionDetails.forEach(function (postDetail) {
      postDetails.push(postDetail);
    });

    // if we have a different keyword than previous then set thePageNumber to 1
    if (this.previousKeyword != keyword) {
      this.thePageNumber = 1;
    }
    this.previousKeyword = keyword;

    var searchRequest = new SearchRequest();
    searchRequest.keyword = keyword;
    searchRequest.priceMin = +priceMin;
    searchRequest.priceMax = +priceMax;
    searchRequest.postDetails = postDetails;
    searchRequest.sortBy = +sortBy;
    searchRequest.pageNumber = this.thePageNumber - 1;
    searchRequest.pageSize = this.thePageSize;

    // now search for the posts using keyword
    this.postService.searchPostsPaginate(searchRequest).subscribe(this.processResult());
  }

  handleCategoryPosts() {
    var categoryId: string = this.route.snapshot.paramMap.get('categoryId');
    var sortBy: string = this.route.snapshot.paramMap.get('sortBy') || "0";
    var priceMin: string = this.route.snapshot.paramMap.get('priceMin');
    var priceMax: string = this.route.snapshot.paramMap.get('priceMax');
    var postDetails: PostDetail[] = [];

    var category = new Category();
    category.id = +categoryId;

    var postDetails: PostDetail[] = [];

    // Get Book Details
    var bookMode = this.route.snapshot.paramMap.has('courseBook');
    if (bookMode) {
      var courseBook: string = this.route.snapshot.paramMap.get('courseBook');
      var bookDetails: PostDetail[] = this.getBookDetailList(courseBook);
      bookDetails.forEach(function (postDetail) {
        postDetails.push(postDetail);
      });
    }

    // Get Housing Details
    var housingMode = this.route.snapshot.paramMap.has('amenities');
    if (housingMode) {
      var amenities: string = this.route.snapshot.paramMap.get('amenities');
      var furnished: string = this.route.snapshot.paramMap.get('furnished');
      var parking: string = this.route.snapshot.paramMap.get('parking');
      var housingDetails: PostDetail[] = this.getHousingDetailList(amenities, furnished, parking);
      housingDetails.forEach(function (postDetail) {
        postDetails.push(postDetail);
      });
    } else {
      // Prepare condition PostDetail list
      var excellent: string = this.route.snapshot.paramMap.get('excellent');
      var lightUsage: string = this.route.snapshot.paramMap.get('lightUsage');
      var visibleUsage: string = this.route.snapshot.paramMap.get('visibleUsage');
      var heavyUsage: string = this.route.snapshot.paramMap.get('heavyUsage');

      // Get Conditional Details
      var conditionDetails: PostDetail[] = this.getConditionList(excellent, lightUsage, visibleUsage, heavyUsage);
      conditionDetails.forEach(function (postDetail) {
        postDetails.push(postDetail);
      });
    }


    var categoryPost = new CategoryPost();
    categoryPost.category = category;
    categoryPost.priceMin = +priceMin;
    categoryPost.priceMax = +priceMax;
    categoryPost.sortBy = +sortBy;
    categoryPost.postDetails = postDetails;
    categoryPost.pageNumber = this.thePageNumber - 1;
    categoryPost.pageSize = this.thePageSize;

    // now search for the posts using keyword
    this.postService.categoryPostsPaginate(categoryPost).subscribe(this.processResult());
  }

  handleAllPosts() {

    var searchRequest = new SearchRequest();
    searchRequest.keyword = '';
    searchRequest.priceMin = 0;
    searchRequest.priceMax = -1;
    searchRequest.postDetails = [];
    searchRequest.sortBy = 0; // Latest Post First
    searchRequest.pageNumber = this.thePageNumber - 1;
    searchRequest.pageSize = this.thePageSize;

    // now search for the posts using keyword
    this.postService.searchPostsPaginate(searchRequest).subscribe(this.processResult());
  }

  processResult() {
    return data => {
      this.posts = data.content;
      this.thePageNumber = data.number + 1;
      this.thePageSize = data.size;
      this.theTotalElements = data.totalElements;

      // Mark as interested
      for (var post of this.posts) {
        if (post.seller.id == this.currentUserId) {
          post.markedInterested = true;
          continue;
        }
        for (var buyer of post.buyers) {
          if (buyer.user.id == this.currentUserId) {
            post.markedInterested = true;
          }
        }
      }
    };
  }

  updatePageSize(pageSize: number) {
    this.thePageSize = pageSize;
    this.thePageNumber = 1;
    this.listPosts();
  }

  getConditionList(excellent: string, lightUsage: string, visibleUsage: string, heavyUsage: string): PostDetail[] {
    var postDetails: PostDetail[] = []

    if (excellent == "1") {
      var postDetail = new PostDetail();
      postDetail.fieldName = "condition";
      postDetail.fieldValue = "Excellent";
      postDetails.push(postDetail)
    }

    if (lightUsage == "1") {
      var postDetail = new PostDetail();
      postDetail.fieldName = "condition";
      postDetail.fieldValue = "Light Usage";
      postDetails.push(postDetail)
    }

    if (visibleUsage == "1") {
      var postDetail = new PostDetail();
      postDetail.fieldName = "condition";
      postDetail.fieldValue = "Visible Usage";
      postDetails.push(postDetail)
    }

    if (heavyUsage == "1") {
      var postDetail = new PostDetail();
      postDetail.fieldName = "condition";
      postDetail.fieldValue = "Heavy Usage";
      postDetails.push(postDetail)
    }

    return postDetails;
  }

  getBookDetailList(courseBook: string): PostDetail[] {
    var postDetails: PostDetail[] = []

    if (courseBook == "1") {
      var postDetail = new PostDetail();
      postDetail.fieldName = "booksCourseBook";
      postDetail.fieldValue = "Yes";
      postDetails.push(postDetail)
    }

    return postDetails;
  }

  getHousingDetailList(amenities: string, furnished: string, parking: string): PostDetail[] {
    var postDetails: PostDetail[] = []

    if (amenities == "1") {
      var postDetail = new PostDetail();
      postDetail.fieldName = "housingAmenities";
      postDetail.fieldValue = "Yes";
      postDetails.push(postDetail)
    }

    if (furnished == "1") {
      var postDetail = new PostDetail();
      postDetail.fieldName = "housingFurnished";
      postDetail.fieldValue = "Yes";
      postDetails.push(postDetail)
    }

    if (parking == "1") {
      var postDetail = new PostDetail();
      postDetail.fieldName = "housingParking";
      postDetail.fieldValue = "Yes";
      postDetails.push(postDetail)
    }

    return postDetails;
  }

  onPostSelected(post: Post) {
    // console.log("OnPostSelected");
    this.router.navigate(['/post-details'], { state: { data: post } });
  }

  onInterested(post: Post) {

    let user = new User();
    user.id = this.currentUserId;

    let buyerRequest = new BuyerRequest();
    buyerRequest.post = post;
    buyerRequest.user = user;

    this.buyerService.markAsInterested(buyerRequest).subscribe(
      data => {
        if (data.statusText == "OK") {
          for (var eachPost of this.posts) {
            if (eachPost.id == post.id) {
              eachPost.markedInterested = true;
            }
          }
        }
      });
  }
}
