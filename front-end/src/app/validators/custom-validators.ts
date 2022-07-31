import {FormControl, FormGroup, ValidationErrors, ValidatorFn} from '@angular/forms';

export class CustomValidators {

  // whitespace validation
  static notOnlyWhitespace(control: FormControl): ValidationErrors {

    // check if string only contains whitespace
    if ((control.value != null) && (control.value.trim().length === 0)) {

      // invalid, return error object
      return { 'notOnlyWhitespace': true };
    }
    else {
      // valid, return null
      return null as any;
    }
  }

// password and confirm password match
//   static mustMatch(password: string, confirm_password: string) {
//     return (formGroup: FormGroup) => {
//       const control = formGroup.controls[password];
//       const matchingControl = formGroup.controls[confirm_password];
//
//       if (matchingControl.errors && !matchingControl.errors['mustMatch']) {
//         return;
//       }
//
//       // set error on matchingControl if validation fails
//       if (control.value !== matchingControl.value) {
//         matchingControl.setErrors({ mustMatch: true });
//       } else {
//         matchingControl.setErrors(null);
//       }
//       return null;
//     };
//   }

}
