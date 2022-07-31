import {
  Component,
  OnInit
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators
} from '@angular/forms';
import {
  Router
} from '@angular/router';
import {
  User
} from 'src/app/common/user';
import {
  EditProfileService
} from 'src/app/services/edit-profile.service';
import {UserService} from "../../services/user.service";
import {
  CustomValidators
} from 'src/app/validators/custom-validators';
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  currentDate = new Date();

  user: User = new User();
  editProfileFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder, private editProfileService: EditProfileService, private router: Router, private toastrService: ToastrService) {}

  ngOnInit(): void {
    const userId = localStorage.getItem("user_id");
    if(userId == 'null'){
      this.router.navigateByUrl(`/login`);
    }else {
      this.handleUserDetails();
      this.editProfileFormGroup = this.formBuilder.group({
        editProfileForm: this.formBuilder.group({

          firstName: new FormControl('',
              [Validators.required, Validators.minLength(2), Validators.maxLength(50), CustomValidators.notOnlyWhitespace]),

          lastName: new FormControl('',
              [Validators.required, Validators.minLength(2), Validators.maxLength(50), CustomValidators.notOnlyWhitespace]),

          dateOfBirth: new FormControl('', [Validators.required]),

          email: new FormControl('',
              [Validators.required]),

          phoneNumber: new FormControl('',
              [Validators.required])
        }),
      })
    }
  }

  handleUserDetails() {

    const currentUserId: number = +localStorage.getItem("user_id");

    this.editProfileService.getUser(currentUserId).subscribe({
      next: Response => {
        this.user = Response.user;
      }
    })
  }

  onSubmit() {
    // console.log("Handling the submit button");

    let user = new User();
    user.firstName = this.editProfileFormGroup.controls['editProfileForm'].value.firstName;
    user.lastName = this.editProfileFormGroup.controls['editProfileForm'].value.lastName;
    user.dateOfBirth = this.editProfileFormGroup.controls['editProfileForm'].value.dateOfBirth;
    user.email = this.editProfileFormGroup.controls['editProfileForm'].value.email;
    user.phoneNumber = this.editProfileFormGroup.controls['editProfileForm'].value.phoneNumber;
    // console.log(user);

    if (this.editProfileFormGroup.invalid) {
      this.editProfileFormGroup.markAllAsTouched();
      // console.log("Form details invalid");
      return;
    }


    this.editProfileService.updateUser(user).subscribe({
      next: response => {
        this.toastrService.success(`${response.message}`, "Success!");
        // alert(`User Registered: ${response.message}`);
      },
      error: err => {
        this.toastrService.error(`${err.message}`, "Error!");
      }
    });
  }

  get firstName() {
    return this.editProfileFormGroup.get('editProfileForm.firstName');
  }
  get lastName() {
    return this.editProfileFormGroup.get('editProfileForm.lastName');
  }
  get email() {
    return this.editProfileFormGroup.get('editProfileForm.email');
  }
  get phoneNumber() {
    return this.editProfileFormGroup.get('editProfileForm.phoneNumber');
  }
  get dateOfBirth() {
    return this.editProfileFormGroup.get('editProfileForm.dateOfBirth');
  }

}
