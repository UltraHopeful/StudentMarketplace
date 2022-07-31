import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {PasswordResetService} from "../../services/password-reset.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-request-reset',
  templateUrl: './request-reset.component.html',
  styleUrls: ['./request-reset.component.css']
})
export class RequestResetComponent implements OnInit {

  requestResetFormGroup: FormGroup

  constructor(private formBuilder: FormBuilder, private passwordResetService: PasswordResetService, private router: Router, private toastrService: ToastrService) {
  }

  ngOnInit(): void {

    this.requestResetFormGroup = this.formBuilder.group({
      forgotPassword: this.formBuilder.group({
        email: new FormControl('',
          [Validators.required, Validators.email])
      }),
    });
  }

  submit() {
    // console.log("Handling the submit button");

    if (this.requestResetFormGroup.invalid) {
      this.requestResetFormGroup.markAllAsTouched();
      return;
    }


    let email = this.requestResetFormGroup.controls['forgotPassword'].value.email;

    // console.log(email);

    // call REST API via the User Login Service
    this.passwordResetService.forgotPassword(email).subscribe({
      next: response => {
        this.toastrService.success(`${response.message}`, "Success!");
        this.router.navigateByUrl(`/login`);
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
    this.requestResetFormGroup.reset();
  }

  get email() {
    return this.requestResetFormGroup.get('forgotPassword.email');
  }

}
