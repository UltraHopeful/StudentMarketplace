import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
// import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { User } from 'src/app/common/user';
import { UserService } from 'src/app/services/user.service';
import { NgbActiveModal, NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { CustomValidators } from 'src/app/validators/custom-validators';
import { Router } from "@angular/router";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  user: User = new User();

  constructor(private userService: UserService, private route: ActivatedRoute, private modalService: NgbModal, private router: Router) { }

  ngOnInit(): void {
    const userId = localStorage.getItem("user_id");
    if (userId == 'null') {
      this.router.navigateByUrl(`/login`);
    } else {
      this.handleUserDetails();
    }
  }

  closeResult = '';

  open(content) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }
  onDelete() {
    this.userService.deleteUserProfile(this.user).subscribe({
      next: response => {

        alert(`User deleted: ${response.message}`);
        localStorage.setItem("user_id", null);
        this.router.navigateByUrl(`/login`);

      },
      error: err => {
        alert(`There was an error: ${err.message}`);
      }
    });

    this.modalService.dismissAll();
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  handleUserDetails() {
    const currentUserId: number = +localStorage.getItem("user_id");
    // console.log(localStorage.getItem("user_id"));
    this.userService.getUser(currentUserId).subscribe({
      next: Response => {
        // console.log("User: " + JSON.stringify(Response));
        // console.log("User: " + JSON.stringify(Response.user));
        this.user = Response.user;
      }
    })
  }
}
