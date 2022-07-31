import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Category } from 'src/app/common/category';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-post-category-menu',
  templateUrl: './post-category-menu.component.html',
  styleUrls: ['./post-category-menu.component.css']
})
export class PostCategoryMenuComponent implements OnInit {

  categoryMode: String = "";
  categoryId: number = 0;
  searchKeyword: String = "";
  postCategories: Category[];
  sortBy: number = 0; // Latest Post

  @ViewChild('keyword') keyword: ElementRef;
  @ViewChild('priceMin') priceMin: ElementRef;
  @ViewChild('priceMax') priceMax: ElementRef;
  @ViewChild('excellent') excellent: ElementRef;
  @ViewChild('lightUsage') lightUsage: ElementRef;
  @ViewChild('visibleUsage') visibleUsage: ElementRef;
  @ViewChild('heavyUsage') heavyUsage: ElementRef;
  @ViewChild('courseBook') courseBook: ElementRef;
  @ViewChild('amenities') amenities: ElementRef;
  @ViewChild('furnished') furnished: ElementRef;
  @ViewChild('parking') parking: ElementRef;

  constructor(private postService: PostService, private router: Router) { }

  ngOnInit(): void {
    this.listPostCategories();
  }

  listPostCategories() {

    this.postService.getPostCategories().subscribe(
      data => {
        this.postCategories = data;
      }
    );
  }

  doSearch() {
    const keyword = this.keyword.nativeElement.value == "" ? "-1" : this.keyword.nativeElement.value;
    const priceMin = this.priceMin.nativeElement.value == "" || this.priceMin.nativeElement.value < 0 ? 0 : this.priceMin.nativeElement.value;
    const priceMax = this.priceMax.nativeElement.value == "" || this.priceMax.nativeElement.value < priceMin ? -1 : this.priceMax.nativeElement.value;;

    var excellent = 0;
    var lightUsage = 0;
    var visibleUsage = 0;
    var heavyUsage = 0;

    if (this.categoryMode != "Housing") {
      excellent = this.excellent.nativeElement.checked ? 1 : 0;
      lightUsage = this.lightUsage.nativeElement.checked ? 1 : 0;
      visibleUsage = this.visibleUsage.nativeElement.checked ? 1 : 0;
      heavyUsage = this.heavyUsage.nativeElement.checked ? 1 : 0;
    }

    this.categoryMode = "";
    this.router.navigateByUrl(`/search/${keyword}/${priceMin}/${priceMax}/${this.sortBy}/${excellent}/${lightUsage}/${visibleUsage}/${heavyUsage}`);
  }

  changeCategory(categoryId: number, categoryName: String) {
    this.keyword.nativeElement.value = "";
    this.categoryMode = categoryName;
    this.categoryId = categoryId;

    const priceMin = this.priceMin.nativeElement.value == "" || this.priceMin.nativeElement.value < 0 ? 0 : this.priceMin.nativeElement.value;
    const priceMax = this.priceMax.nativeElement.value == "" || this.priceMax.nativeElement.value < priceMin ? -1 : this.priceMax.nativeElement.value;

    var excellent = 0;
    var lightUsage = 0;
    var visibleUsage = 0;
    var heavyUsage = 0;

    // Housing
    if (this.categoryMode == "Housing") {
      var amenities = 0;
      var furnished = 0;
      var parking = 0;

      if (this.amenities != null) {
        amenities = this.amenities.nativeElement.checked ? 1 : 0;
        furnished = this.furnished.nativeElement.checked ? 1 : 0;
        parking = this.parking.nativeElement.checked ? 1 : 0;

      }

      this.router.navigateByUrl(`/category/${categoryId}/${priceMin}/${priceMax}/${this.sortBy}/${amenities}/${furnished}/${parking}`);
    } else if (this.categoryMode == "Books") { // Books

      // Conditions
      if (this.excellent != null) {
        excellent = this.excellent.nativeElement.checked ? 1 : 0;
        lightUsage = this.lightUsage.nativeElement.checked ? 1 : 0;
        visibleUsage = this.visibleUsage.nativeElement.checked ? 1 : 0;
        heavyUsage = this.heavyUsage.nativeElement.checked ? 1 : 0;
      }

      var courseBook = 0;
      if (this.courseBook != null) {
        var courseBook = this.courseBook.nativeElement.checked ? 1 : 0;
      }
      this.router.navigateByUrl(`/category/${categoryId}/${priceMin}/${priceMax}/${this.sortBy}/${excellent}/${lightUsage}/${visibleUsage}/${heavyUsage}/${courseBook}`);
    } else { // Furniture and Electronics

      // Conditions
      if (this.excellent != null) {
        excellent = this.excellent.nativeElement.checked ? 1 : 0;
        lightUsage = this.lightUsage.nativeElement.checked ? 1 : 0;
        visibleUsage = this.visibleUsage.nativeElement.checked ? 1 : 0;
        heavyUsage = this.heavyUsage.nativeElement.checked ? 1 : 0;
      }

      this.router.navigateByUrl(`/category/${categoryId}/${priceMin}/${priceMax}/${this.sortBy}/${excellent}/${lightUsage}/${visibleUsage}/${heavyUsage}`);
    }
  }

  onFilterChanged() {
    // console.log('Sort By: ' + this.sortBy);
    if(this.categoryMode == "") {
      this.doSearch();
    } else {
      this.changeCategory(this.categoryId, this.categoryMode);
    }
  }
}
