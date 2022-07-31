import { isNgTemplate } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { AngularFireStorage } from '@angular/fire/compat/storage';
import * as uuid from 'uuid';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AddPost } from 'src/app/common/add-post';
import { Category } from 'src/app/common/category';
import { Post } from 'src/app/common/post';
import { PostDetail } from 'src/app/common/post-detail';
import { User } from 'src/app/common/user';
import { PostService } from 'src/app/services/post.service';
import { CustomValidators } from 'src/app/validators/custom-validators';
import { Media } from 'src/app/common/media';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent implements OnInit {

  addPostFormGroup: FormGroup;

  categories: Category[] = [];

  categoryMode: string = "";
  bookCoursebookMode: boolean = false;
  housingTypeMode: boolean = false;
  downloadUrl: string;

  constructor(private formBuilder: FormBuilder, private postService: PostService, private router: Router, private firebaseStorage: AngularFireStorage,  private toastrService: ToastrService) { }

  ngOnInit(): void {
    const userId = localStorage.getItem("user_id");
    if (userId == 'null') {
      this.router.navigateByUrl(`/login`);
    }
    this.addPostFormGroup = this.formBuilder.group({
      addPost: this.formBuilder.group({
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
        media: new FormControl('',
        [Validators.required])
      }),
      addFurniture: this.formBuilder.group({
        furnitureType: new FormControl(''),
        furnitureWeight: new FormControl(''),
        furnitureLength: new FormControl(''),
        furnitureWidth: new FormControl(''),
        furnitureHeight: new FormControl(''),
        furnitureColor: new FormControl(''),
        furnitureAge: new FormControl(''),
        furnitureCondition: new FormControl('')
      }),
      addElectronics: this.formBuilder.group({
        electronicsType: new FormControl(''),
        electronicsBrand: new FormControl(''),
        electronicsColor: new FormControl(''),
        electronicsCondition: new FormControl(''),
        electronicsAge: new FormControl('')
      }),
      addBooks: this.formBuilder.group({
        booksCourseBook: new FormControl(''),
        booksCourseName: new FormControl(''),
        booksCourseLevel: new FormControl(''),
        booksCourseCondition: new FormControl('')
      }),
      addHousing: this.formBuilder.group({
        housingLocation: new FormControl(''),
        housingFloor: new FormControl(''),
        housingType: new FormControl(''),
        housingRooms: new FormControl(''),
        housingFurnished: new FormControl(''),
        housingAvailability: new FormControl(''),
        housingParking: new FormControl(''),
        housingAmenities: new FormControl('')
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

    if (this.addPostFormGroup.invalid) {
      // console.log("Invalid Form");
      this.addPostFormGroup.markAllAsTouched();
      return;
    }

    let post = new Post();

    let user = new User();
    user.id = +localStorage.getItem("user_id");
    user.email = localStorage.getItem("user_email");

    post.title = this.addPostFormGroup.controls['addPost'].value.title;
    post.description = this.addPostFormGroup.controls['addPost'].value.description;
    post.price = this.addPostFormGroup.controls['addPost'].value.price;
    post.category = this.addPostFormGroup.controls['addPost'].value.category;
    post.negotiable = this.addPostFormGroup.controls['addPost'].value.negotiable;
    post.seller = user
    post.status = "Open";

    let addPost = new AddPost();
    addPost.post = post;

    // Get Post Details
    const postDetails = this.getCategoryDetails();
    postDetails.forEach((postDetail) => {
      // console.log("Post Detail => Field: " + postDetail.fieldName + " | Value: " + postDetail.fieldValue);
    });
    // console.log("Post Details: " + postDetails.toString);

    addPost.postDetails = postDetails;

    // Create Media - Doing for only one image now
    let media: Media[] = [];
    var imageDetail = new Media();
    imageDetail.location = this.downloadUrl;
    media.push(imageDetail);

    addPost.media = media;

    // call REST API via the Post Service
    this.postService.addPost(addPost).subscribe(
      data => {
        if (data.statusText == "OK") {
          this.toastrService.success(`${data.body.message}`, "Success!");
          this.resetForm();
        } else {
          this.toastrService.error(`${data.err}`, "Error!");
        }
      });
  }

  onFileChange(event) {
    if (event.target.files.length > 0) {

      const file: File = event.target.files[0];
      const metaData = { 'contentType': file.type };

      const storRef = this.firebaseStorage.storage.ref('/PostImages/' + uuid.v4() + file.name);
      const uploadTask = storRef.put(file, metaData).then((snapshot) => {
        snapshot.ref.getDownloadURL().then(downloadUrl => {
          this.downloadUrl = downloadUrl
        });
      });
    }
  }

  resetForm() {
    // reset the form
    this.addPostFormGroup.reset();

    // navigate to the login page
    this.router.navigateByUrl("/posts");
  }

  // When Post Category selection changes
  onCategorySelected() {
    const addPost = this.addPostFormGroup.get('addPost');
    const selectedCategory = addPost.value.category;

    this.categoryMode = selectedCategory.categoryName;

    // Remove previous Category Validators
    this.clearCategoryValidators();

    // Change Category Mode
    // console.log("Category Selected: " + this.categoryMode);

    // Add selected Category Validators
    this.addCategoryValidators();
  }

  // When bookCoursebook selection changes
  onBooksCourseBookSelected() {
    const addBooks = this.addPostFormGroup.get('addBooks');
    const selectedChoice = addBooks.value.booksCourseBook;

    this.bookCoursebookMode = selectedChoice == 'Yes' ? true : false;

    if (this.bookCoursebookMode) {
      // Add selected Category Validators
      this.booksCourseName.addValidators([Validators.required, Validators.minLength(2), CustomValidators.notOnlyWhitespace]);
      this.booksCourseLevel.addValidators([Validators.required]);
    } else {
      // Clear Validators
      this.booksCourseName.clearValidators();
      this.booksCourseLevel.clearValidators();

      // Update Form control
      this.booksCourseName.updateValueAndValidity();
      this.booksCourseLevel.updateValueAndValidity();
    }
  }

  // When housingType selection changes
  onHousingTypeSelected() {
    const addHousing = this.addPostFormGroup.get('addHousing');
    const selectedChoice = addHousing.value.housingType;

    this.housingTypeMode = selectedChoice == 'Bedroom' ? true : false;

    if (this.housingTypeMode) {
      // Add selected Category Validators
      this.housingRooms.addValidators([Validators.required]);
    } else {
      // Clear Validators
      this.housingRooms.clearValidators();

      // Update Form control
      this.housingRooms.updateValueAndValidity();
    }
  }

  // Clear the field Validators
  clearCategoryValidators() {
    // console.log("Clearing Validators");

    /*************************************************
     * Furniture
     *************************************************/

    // Clear Validators
    this.furnitureType.clearValidators();
    this.furnitureWeight.clearValidators();
    this.furnitureLength.clearValidators();
    this.furnitureWidth.clearValidators();
    this.furnitureHeight.clearValidators();
    this.furnitureColor.clearValidators();
    this.furnitureAge.clearValidators();
    this.furnitureCondition.clearValidators();

    // Update Form control
    this.furnitureType.updateValueAndValidity();
    this.furnitureWeight.updateValueAndValidity();
    this.furnitureLength.updateValueAndValidity();
    this.furnitureWidth.updateValueAndValidity();
    this.furnitureHeight.updateValueAndValidity();
    this.furnitureColor.updateValueAndValidity();
    this.furnitureAge.updateValueAndValidity();
    this.furnitureCondition.updateValueAndValidity();

    /*************************************************
     * Electronics
     *************************************************/

    // Clear Validators
    this.electronicsType.clearValidators();
    this.electronicsBrand.clearValidators();
    this.electronicsColor.clearValidators();
    this.electronicsCondition.clearValidators();
    this.electronicsAge.clearValidators();

    // Update Form control
    this.electronicsType.updateValueAndValidity();
    this.electronicsBrand.updateValueAndValidity();
    this.electronicsColor.updateValueAndValidity();
    this.electronicsCondition.updateValueAndValidity();
    this.electronicsAge.updateValueAndValidity();

    /*************************************************
     * Books
     *************************************************/

    // Clear Validators
    this.booksCourseBook.clearValidators();
    this.booksCourseName.clearValidators();
    this.booksCourseLevel.clearValidators();
    this.booksCourseCondition.clearValidators();

    // Update Form control
    this.booksCourseBook.updateValueAndValidity();
    this.booksCourseName.updateValueAndValidity();
    this.booksCourseLevel.updateValueAndValidity();
    this.booksCourseCondition.updateValueAndValidity();

    // Change to default mode
    this.bookCoursebookMode = false;

    /*************************************************
     * Housing
     *************************************************/

    // Clear Validators
    this.housingLocation.clearValidators();
    this.housingFloor.clearValidators();
    this.housingType.clearValidators();
    this.housingRooms.clearValidators();
    this.housingFurnished.clearValidators();
    this.housingAvailability.clearValidators();
    this.housingParking.clearValidators();
    this.housingAmenities.clearValidators();

    // Update Form control
    this.housingLocation.updateValueAndValidity();
    this.housingFloor.updateValueAndValidity();
    this.housingType.updateValueAndValidity();
    this.housingRooms.updateValueAndValidity();
    this.housingFurnished.updateValueAndValidity();
    this.housingAvailability.updateValueAndValidity();
    this.housingParking.updateValueAndValidity();
    this.housingAmenities.updateValueAndValidity();

    // Change to default mode
    this.housingTypeMode = false;
  }

  // Add the field Validators for the selected category
  addCategoryValidators() {
    switch (this.categoryMode) {
      case "Furniture":
        // console.log('Adding Furniture Validators');
        this.furnitureType.addValidators([Validators.required, Validators.minLength(2), CustomValidators.notOnlyWhitespace]);
        this.furnitureWeight.addValidators([Validators.required]);
        this.furnitureLength.addValidators([Validators.required]);
        this.furnitureWidth.addValidators([Validators.required]);
        this.furnitureHeight.addValidators([Validators.required]);
        this.furnitureColor.addValidators([Validators.required, Validators.minLength(2), CustomValidators.notOnlyWhitespace]);
        this.furnitureAge.addValidators([Validators.required]);
        this.furnitureCondition.addValidators([Validators.required]);
        break;
      case "Electronics":
        // console.log('Adding Electronics Validators');
        this.electronicsType.addValidators([Validators.required, Validators.minLength(2), CustomValidators.notOnlyWhitespace]);
        this.electronicsBrand.addValidators([Validators.required, Validators.minLength(2), CustomValidators.notOnlyWhitespace]);
        this.electronicsColor.addValidators([Validators.required, Validators.minLength(2), CustomValidators.notOnlyWhitespace]);
        this.electronicsCondition.addValidators([Validators.required]);
        this.electronicsAge.addValidators([Validators.required]);
        break;
      case "Books":
        // console.log('Adding Books Validators');
        this.booksCourseBook.addValidators([Validators.required]);
        this.booksCourseCondition.addValidators([Validators.required]);
        break;
      case "Housing":
        // console.log('Adding Housing Validators');
        this.housingLocation.addValidators([Validators.required, Validators.minLength(2), CustomValidators.notOnlyWhitespace]);
        this.housingFloor.addValidators([Validators.required]);
        this.housingType.addValidators([Validators.required]);
        this.housingFurnished.addValidators([Validators.required]);
        this.housingAvailability.addValidators([Validators.required]);
        this.housingParking.addValidators([Validators.required]);
        this.housingAmenities.addValidators([Validators.required]);
        break;
      default:
        // console.log('Invalid Catefory Selected');
        break;
    }
  }

  // Get the post selected category details
  getCategoryDetails(): PostDetail[] {
    switch (this.categoryMode) {
      case "Furniture":
        // console.log('Getting Furniture Details');
        return this.getFurnitureDetails();
      case "Electronics":
        // console.log('Getting Electronics Details');
        return this.getElectronicDetails();
      case "Books":
        // console.log('Getting Books Details');
        return this.getBookDetails();
      case "Housing":
        // console.log('Getting Housing Details');
        return this.getHousingDetails();
      default:
        // console.log('Invalid Catefory Selected');
        return [];
    }
  }

  // On Form Submit, if furniture is selected
  getFurnitureDetails(): PostDetail[] {

    // Furniture Type
    const furnitureType: PostDetail = new PostDetail();
    furnitureType.fieldName = 'furnitureType'
    furnitureType.fieldValue = this.furnitureType.value;

    // Furniture Weight
    const furnitureWeight: PostDetail = new PostDetail();
    furnitureWeight.fieldName = 'furnitureWeight'
    furnitureWeight.fieldValue = this.furnitureWeight.value;

    // Furniture Length
    const furnitureLength: PostDetail = new PostDetail();
    furnitureLength.fieldName = 'furnitureLength'
    furnitureLength.fieldValue = this.furnitureLength.value;

    // Furniture Width
    const furnitureWidth: PostDetail = new PostDetail();
    furnitureWidth.fieldName = 'furnitureWidth'
    furnitureWidth.fieldValue = this.furnitureWidth.value;

    // Furniture Height
    const furnitureHeight: PostDetail = new PostDetail();
    furnitureHeight.fieldName = 'furnitureHeight'
    furnitureHeight.fieldValue = this.furnitureHeight.value;

    // Furniture Color
    const furnitureColor: PostDetail = new PostDetail();
    furnitureColor.fieldName = 'furnitureColor'
    furnitureColor.fieldValue = this.furnitureColor.value;

    // Furniture Age
    const furnitureAge: PostDetail = new PostDetail();
    furnitureAge.fieldName = 'furnitureAge'
    furnitureAge.fieldValue = this.furnitureAge.value;

    // Furniture Condition
    const furnitureCondition: PostDetail = new PostDetail();
    furnitureCondition.fieldName = 'condition' // Using generic field name to get help while filtering by condition
    furnitureCondition.fieldValue = this.furnitureCondition.value;

    // Return Post Detail Array
    const postDetails: PostDetail[] = [];
    postDetails.push(furnitureType);
    postDetails.push(furnitureWeight);
    postDetails.push(furnitureLength);
    postDetails.push(furnitureWidth);
    postDetails.push(furnitureHeight);
    postDetails.push(furnitureColor);
    postDetails.push(furnitureAge);
    postDetails.push(furnitureCondition);

    return postDetails;
  }

  // On Form Submit, if electronics is selected
  getElectronicDetails(): PostDetail[] {

    // Electronics Type
    const electronicsType: PostDetail = new PostDetail();
    electronicsType.fieldName = 'electronicsType'
    electronicsType.fieldValue = this.electronicsType.value;

    // Electronics Brand
    const electronicsBrand: PostDetail = new PostDetail();
    electronicsBrand.fieldName = 'electronicsBrand'
    electronicsBrand.fieldValue = this.electronicsBrand.value;

    // Electronics Color
    const electronicsColor: PostDetail = new PostDetail();
    electronicsColor.fieldName = 'electronicsColor'
    electronicsColor.fieldValue = this.electronicsColor.value;

    // Electronics Condition
    const electronicsCondition: PostDetail = new PostDetail();
    electronicsCondition.fieldName = 'condition' // Using generic field name to get help while filtering by condition
    electronicsCondition.fieldValue = this.electronicsCondition.value;

    // Electronics Age
    const electronicsAge: PostDetail = new PostDetail();
    electronicsAge.fieldName = 'electronicsAge'
    electronicsAge.fieldValue = this.electronicsAge.value;

    // Return Post Detail Array
    const postDetails: PostDetail[] = [];
    postDetails.push(electronicsType);
    postDetails.push(electronicsBrand);
    postDetails.push(electronicsColor);
    postDetails.push(electronicsCondition);
    postDetails.push(electronicsAge);

    return postDetails;
  }

  // On Form Submit, if book is selected
  getBookDetails(): PostDetail[] {

    // Books Course Book
    const booksCourseBook: PostDetail = new PostDetail();
    booksCourseBook.fieldName = 'booksCourseBook'
    booksCourseBook.fieldValue = this.booksCourseBook.value;

    // Books Course Condition
    const booksCourseCondition: PostDetail = new PostDetail();
    booksCourseCondition.fieldName = 'condition' // Using generic field name to get help while filtering by condition
    booksCourseCondition.fieldValue = this.booksCourseCondition.value;

    // Return Post Detail Array
    const postDetails: PostDetail[] = [];
    postDetails.push(booksCourseBook);
    postDetails.push(booksCourseCondition);

    if (this.bookCoursebookMode) {
      // Books Course Name
      const booksCourseName: PostDetail = new PostDetail();
      booksCourseName.fieldName = 'booksCourseName'
      booksCourseName.fieldValue = this.booksCourseName.value;

      // Books Course Level
      const booksCourseLevel: PostDetail = new PostDetail();
      booksCourseLevel.fieldName = 'booksCourseLevel' // Using generic field name to get help while filtering by condition
      booksCourseLevel.fieldValue = this.booksCourseLevel.value;

      postDetails.push(booksCourseName);
      postDetails.push(booksCourseLevel);
    }




    return postDetails;
  }

  // On Form Submit, if housing is selected
  getHousingDetails(): PostDetail[] {

    // Housing Location
    const housingLocation: PostDetail = new PostDetail();
    housingLocation.fieldName = 'housingLocation'
    housingLocation.fieldValue = this.housingLocation.value;

    // Housing Floor
    const housingFloor: PostDetail = new PostDetail();
    housingFloor.fieldName = 'housingFloor'
    housingFloor.fieldValue = this.housingFloor.value;

    // Housing Type
    const housingType: PostDetail = new PostDetail();
    housingType.fieldName = 'housingType'
    housingType.fieldValue = this.housingType.value;

    // Housing Furnished
    const housingFurnished: PostDetail = new PostDetail();
    housingFurnished.fieldName = 'housingFurnished'
    housingFurnished.fieldValue = this.housingFurnished.value;

    // Housing Availability
    const housingAvailability: PostDetail = new PostDetail();
    housingAvailability.fieldName = 'housingAvailability'
    housingAvailability.fieldValue = this.housingAvailability.value;

    // Housing Parking
    const housingParking: PostDetail = new PostDetail();
    housingParking.fieldName = 'housingParking'
    housingParking.fieldValue = this.housingParking.value;

    // Housing Amenities
    const housingAmenities: PostDetail = new PostDetail();
    housingAmenities.fieldName = 'housingAmenities'
    housingAmenities.fieldValue = this.housingAmenities.value;

    // Return Post Detail Array
    const postDetails: PostDetail[] = [];
    postDetails.push(housingLocation);
    postDetails.push(housingFloor);
    postDetails.push(housingType);
    postDetails.push(housingFurnished);
    postDetails.push(housingAvailability);
    postDetails.push(housingParking);
    postDetails.push(housingAmenities);

    if (this.housingTypeMode) {

      // Housing Rooms
      const housingRooms: PostDetail = new PostDetail();
      housingRooms.fieldName = 'housingRooms'
      housingRooms.fieldValue = this.housingRooms.value;

      postDetails.push(housingRooms);
    }

    return postDetails;
  }

  /*****************************************************
   * GETTERS
   *****************************************************/

  // Post Fields
  get title() { return this.addPostFormGroup.get('addPost.title'); }
  get description() { return this.addPostFormGroup.get('addPost.description'); }
  get price() { return this.addPostFormGroup.get('addPost.price'); }
  get category() { return this.addPostFormGroup.get('addPost.category'); }
  get negotiable() { return this.addPostFormGroup.get('addPost.negotiable'); }
  get media() { return this.addPostFormGroup.get('addPost.media'); }

  // Furniture Fields
  get furnitureType() { return this.addPostFormGroup.get('addFurniture.furnitureType'); }
  get furnitureWeight() { return this.addPostFormGroup.get('addFurniture.furnitureWeight'); }
  get furnitureLength() { return this.addPostFormGroup.get('addFurniture.furnitureLength'); }
  get furnitureWidth() { return this.addPostFormGroup.get('addFurniture.furnitureWidth'); }
  get furnitureHeight() { return this.addPostFormGroup.get('addFurniture.furnitureHeight'); }
  get furnitureColor() { return this.addPostFormGroup.get('addFurniture.furnitureColor'); }
  get furnitureAge() { return this.addPostFormGroup.get('addFurniture.furnitureAge'); }
  get furnitureCondition() { return this.addPostFormGroup.get('addFurniture.furnitureCondition'); }

  // Electronics Fields
  get electronicsType() { return this.addPostFormGroup.get('addElectronics.electronicsType'); }
  get electronicsBrand() { return this.addPostFormGroup.get('addElectronics.electronicsBrand'); }
  get electronicsColor() { return this.addPostFormGroup.get('addElectronics.electronicsColor'); }
  get electronicsCondition() { return this.addPostFormGroup.get('addElectronics.electronicsCondition'); }
  get electronicsAge() { return this.addPostFormGroup.get('addElectronics.electronicsAge'); }

  // Books Fields
  get booksCourseBook() { return this.addPostFormGroup.get('addBooks.booksCourseBook'); }
  get booksCourseName() { return this.addPostFormGroup.get('addBooks.booksCourseName'); }
  get booksCourseLevel() { return this.addPostFormGroup.get('addBooks.booksCourseLevel'); }
  get booksCourseCondition() { return this.addPostFormGroup.get('addBooks.booksCourseCondition'); }

  // Housing Fields
  get housingLocation() { return this.addPostFormGroup.get('addHousing.housingLocation'); }
  get housingFloor() { return this.addPostFormGroup.get('addHousing.housingFloor'); }
  get housingType() { return this.addPostFormGroup.get('addHousing.housingType'); }
  get housingRooms() { return this.addPostFormGroup.get('addHousing.housingRooms'); }
  get housingFurnished() { return this.addPostFormGroup.get('addHousing.housingFurnished'); }
  get housingAvailability() { return this.addPostFormGroup.get('addHousing.housingAvailability'); }
  get housingParking() { return this.addPostFormGroup.get('addHousing.housingParking'); }
  get housingAmenities() { return this.addPostFormGroup.get('addHousing.housingAmenities'); }
}
