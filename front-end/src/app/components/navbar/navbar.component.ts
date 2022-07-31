import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(private router: Router) { }

  userFirstName: string

  isNavbarCollapsed = true;

  ngOnInit(): void {
    this.userFirstName = localStorage.getItem("user_firstName");
  }

  addPost() {
    this.router.navigateByUrl(`/add-post`);
  }

  userProfile() {
    this.router.navigateByUrl(`/user-profile`);
  }

  logout() {
    localStorage.removeItem("user_id");
    localStorage.removeItem("user_email");
    localStorage.removeItem("user_firstName");
    this.router.navigateByUrl(`/login`);
  }
}