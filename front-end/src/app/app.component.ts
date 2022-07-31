import { Component } from '@angular/core';
import { Router } from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = 'marketplace-frontend';
  constructor(private router: Router) {
  }

  displayFilterSidebar() {
    return !(this.router.url.startsWith('/posts')
      || this.router.url.startsWith('/search')
      || this.router.url.startsWith('/category'));
  }

  hideNavBar() {
    return (this.router.url.startsWith('/login')
      || this.router.url.startsWith('/registration')
      || this.router.url.startsWith('/forgot-password')
      || this.router.url.startsWith('/reset-password'));
  }
}