import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {User} from "../../common/user";
import {ChangePasswordService} from "../../services/change-password.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  user: any;
  changePasswordFormGroup: FormGroup;

  constructor(private formBuilder: FormBuilder, private changePasswordService: ChangePasswordService, private router: Router, private route: ActivatedRoute, private toastrService: ToastrService) {
  }

  ngOnInit(): void {

    const userId = localStorage.getItem("user_id");
    if (userId == 'null') {
      this.router.navigateByUrl(`/login`);
    } else {
      this.changePasswordFormGroup = this.formBuilder.group({

        changePassword: this.formBuilder.group({
          currentPassword: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(25)]),
          newPassword: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(25)]),
          confirmPassword: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(25)])

        }),
      });
    }
  }

  onSubmit() {
    // console.log("Handling the submit button");

    if (this.changePasswordFormGroup.invalid) {
      this.changePasswordFormGroup.markAllAsTouched();
      return;
    }
    let Old_Password;
    let New_Password;
    let user: User = new User();
    const currentUserId: number = +localStorage.getItem("user_id");
    user.id = currentUserId;
    user.firstName = localStorage.getItem("user_firstName");
    user.email = localStorage.getItem("user_email");

    Old_Password = this.changePasswordFormGroup.controls['changePassword'].value.currentPassword;
    New_Password = this.changePasswordFormGroup.controls['changePassword'].value.newPassword;
    // console.log(Old_Password, New_Password);
    // console.log(user);
    this.changePasswordService.updatePassword(user.id, user.email, Old_Password, New_Password).subscribe({
      next: Response => {
        let message: string = Response.message;
        if (message.search("not") > 0) {
          this.toastrService.error("Password Reset Unsuccessful!", "Error!");
        } else {
          this.toastrService.success("Password Changed!", "Success!");
          this.user = Response.user;
        }
      },
      error: err => {
        this.toastrService.error(`${err.message}`, "Error!");
      }
    });
  }

  get currentPassword() {
    return this.changePasswordFormGroup.get('changePassword.currentPassword');
  }

  get newPassword() {
    return this.changePasswordFormGroup.get('changePassword.newPassword');
  }

  get confirmPassword() {
    return this.changePasswordFormGroup.get('changePassword.confirmPassword');
  }

}
