import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {User} from "../../common/user";
import {PasswordResetService} from "../../services/password-reset.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-reset-response',
  templateUrl: './reset-response.component.html',
  styleUrls: ['./reset-response.component.css']
})
export class ResetResponseComponent implements OnInit {

  resetResponseFormGroup: FormGroup;
  isTokenVerified: boolean;
  userEmail: string;
  token: string = "";

  constructor(private formBuilder: FormBuilder, private passwordResetService: PasswordResetService, private router: Router, private route: ActivatedRoute, private toastrService: ToastrService) {
  }

  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {
      // console.log(params['token']);
      this.token = params['token'];
    })

    this.passwordResetService.checkResetPasswordLink(this.token).subscribe({
      next: response => {
        // console.log(response.status)
        // console.log(response)
        if (response.status == 200) {
          // console.log("Verified");
          // console.log(response.body.userMail)
          this.userEmail = response.body.userMail;
          this.isTokenVerified = true;
        } else if (response.status == 202) {
          // console.log("Expired link");
          this.isTokenVerified = false;
          this.resetResponseFormGroup.disable();
        } else if (response.status == 203) {
          // console.log("Broken link");
          this.isTokenVerified = false;
          this.resetResponseFormGroup.disable();
        }
      }
    });

    this.resetResponseFormGroup = this.formBuilder.group({
      resetPassword: this.formBuilder.group({
        email: new FormControl('',
          [Validators.required, Validators.email]),
        password: new FormControl('',
          [Validators.required]),
        confirm_password: new FormControl('',
          [Validators.required])
      }),
    });
  }

  Submit() {
    // console.log("Handling the submit button");

    if (this.resetResponseFormGroup.invalid) {
      this.resetResponseFormGroup.markAllAsTouched();
      return;
    }

    let user = new User();
    user.email = this.resetResponseFormGroup.controls['resetPassword'].value.email;
    user.password = this.resetResponseFormGroup.controls['resetPassword'].value.password;

    // console.log(user);

    // call REST API via the user reset password token check
    this.passwordResetService.updatePassword(user, this.token).subscribe({
      next: response => {
        this.toastrService.success(`${response.message}`, "Success!");

        // reset form
        this.resetForm();

      },
      error: err => {
        this.toastrService.error(`${err.message}`, "Error!");
      }
    });
  }

  resetForm() {
    // reset the form
    this.resetResponseFormGroup.reset();
    // navigate to the login page
    this.router.navigateByUrl("/login");
  }

  get email() {
    return this.resetResponseFormGroup.get('resetResponse.email');
  }

  get password() {
    return this.resetResponseFormGroup.get('resetResponse.password');
  }

  get confirm_password() {
    return this.resetResponseFormGroup.get('resetResponse.confirm_password');
  }
}
