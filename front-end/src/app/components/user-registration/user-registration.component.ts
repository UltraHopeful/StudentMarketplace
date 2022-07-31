import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/common/user';
import { UserRegistrationService } from 'src/app/services/user-registration.service';
import { CustomValidators } from 'src/app/validators/custom-validators';
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit {

  currentDate = new Date();

  userRegistrationFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder, private userRegistrationService: UserRegistrationService, private router: Router, private toastrService: ToastrService) { }

  ngOnInit(): void {

    this.userRegistrationFormGroup = this.formBuilder.group({
      userRegistration: this.formBuilder.group({
        firstName: new FormControl('',
          [Validators.required, Validators.minLength(2), Validators.maxLength(50), CustomValidators.notOnlyWhitespace]),
        lastName: new FormControl('',
          [Validators.required, Validators.minLength(2), Validators.maxLength(50), CustomValidators.notOnlyWhitespace]),
        email: new FormControl('',
          [Validators.required, Validators.email]),
        phoneNumber: new FormControl('',
          [Validators.required, Validators.pattern('\\+[0-9]{1,2}\\s[0-9]{3}-[0-9]{3}-[0-9]{4}$')]),
        dateOfBirth: new FormControl('',
          [Validators.required]),
        password: new FormControl('',
          [Validators.required, Validators.minLength(8), Validators.maxLength(25)]),
        confirmPassword: new FormControl('',[Validators.required])}),
  //    validators: CustomValidators.mustMatch('password', 'confirmPassword')
    })
  }

  onSubmit() {
    // console.log("Handling the submit button");

    if (this.userRegistrationFormGroup.controls['userRegistration'].value.password
      != this.userRegistrationFormGroup.controls['userRegistration'].value.confirmPassword) {
      alert("Passwords don't match");
      this.toastrService.error(`Passwords do not match`, "Error!");
      return;
    }

    if (this.userRegistrationFormGroup.invalid) {
      this.userRegistrationFormGroup.markAllAsTouched();
      return;
    }

    let user = new User();
    user.firstName = this.userRegistrationFormGroup.controls['userRegistration'].value.firstName;
    user.lastName = this.userRegistrationFormGroup.controls['userRegistration'].value.lastName;
    user.email = this.userRegistrationFormGroup.controls['userRegistration'].value.email;
    user.phoneNumber = this.userRegistrationFormGroup.controls['userRegistration'].value.phoneNumber;
    user.dateOfBirth = this.userRegistrationFormGroup.controls['userRegistration'].value.dateOfBirth;
    user.password = this.userRegistrationFormGroup.controls['userRegistration'].value.password;

    // console.log(user);

    // call REST API via the User Registration Service
    this.userRegistrationService.registerUser(user).subscribe({
      next: response => {
        this.toastrService.success(`${response.message}`, "User Registered");
        // reset form
        this.resetForm();
      },
      error: err => {
        this.toastrService.error(`"${err.message}`, "Error!");
      }
    });
  }

  resetForm() {
    // reset the form
    this.userRegistrationFormGroup.reset();

    // navigate to the login page
    this.router.navigateByUrl("/login");
  }

  get firstName() { return this.userRegistrationFormGroup.get('userRegistration.firstName'); }
  get lastName() { return this.userRegistrationFormGroup.get('userRegistration.lastName'); }
  get email() { return this.userRegistrationFormGroup.get('userRegistration.email'); }
  get phoneNumber() { return this.userRegistrationFormGroup.get('userRegistration.phoneNumber'); }
  get dateOfBirth() { return this.userRegistrationFormGroup.get('userRegistration.dateOfBirth'); }
  get password() { return this.userRegistrationFormGroup.get('userRegistration.password'); }
  get confirmPassword(){return this.userRegistrationFormGroup.get('userRegistration.confirmPassword');}

}
