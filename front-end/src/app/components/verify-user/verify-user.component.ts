import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UserRegistrationService} from "../../services/user-registration.service";

@Component({
  selector: 'app-verify-user',
  templateUrl: './verify-user.component.html',
  styleUrls: ['./verify-user.component.css']
})
export class VerifyUserComponent implements OnInit {

  isUserVerified: boolean;
  token : string = "" ;
  constructor(private userRegisterService: UserRegistrationService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      // console.log(params['token']);
      this.token = params['token'];
    })

    this.userRegisterService.verifyUser(this.token).subscribe({
      next:response => {
        // console.log(response.status)
        if(response.status == 200){
          // console.log("Verified");
          this.isUserVerified = true;
        }
        else if(response.status == 202){
          // console.log("Broken link");
          this.isUserVerified = false;
        }
      }
    });
  }

}
