import { Component, OnInit } from '@angular/core';
import { UserLoginService } from 'src/app/services/user-login.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/common/user';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  userLoginFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder, private userLoginService: UserLoginService, private router: Router, private toastrService: ToastrService) { }

  ngOnInit(): void {

    this.userLoginFormGroup = this.formBuilder.group({
      userLogin: this.formBuilder.group({
        email: new FormControl('',
          [Validators.required, Validators.email]),
        password: new FormControl('',
          [Validators.required])
      }),
    });

  }

  onSubmit() {
    // console.log("Handling the submit button");

    if (this.userLoginFormGroup.invalid) {
      this.userLoginFormGroup.markAllAsTouched();
      return;
    }

    let user = new User();
    user.email = this.userLoginFormGroup.controls['userLogin'].value.email;
    user.password = this.userLoginFormGroup.controls['userLogin'].value.password;

    // console.log(user);

    // call REST API via the User Login Service
    this.userLoginService.loginUser(user).subscribe({
      next: response => {
        if(response.status == 200){

          if(response.body.user != null) {
            localStorage.setItem("user_id", response.body.user.id);
            localStorage.setItem("user_email", response.body.user.email);
            localStorage.setItem("user_firstName", response.body.user.firstName);
            this.toastrService.success(`${response.body.message}`, "Success!");
            this.router.navigateByUrl(`/posts`);
          } else {
            this.toastrService.warning(`${response.body.message}`, "Warning!");
          }

          // reset form
          this.resetForm();
        }
        else if(response.status == 202){
          this.toastrService.warning(`${response.body.message}`, "Warniing!");
          this.router.navigateByUrl(`/forgot-password`);
        }

      },
      error: err => {
        this.toastrService.error(`${err.message}`, "Error!");
      }
    });
  }

  resetForm() {
    // reset the form
    this.userLoginFormGroup.reset();
  }

  get email() { return this.userLoginFormGroup.get('userLogin.email'); }
  get password() { return this.userLoginFormGroup.get('userLogin.password'); }

}