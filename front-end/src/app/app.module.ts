import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { Routes, RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';

import { AngularFireModule } from '@angular/fire/compat';
import { AngularFirestoreModule } from '@angular/fire/compat/firestore';
import { environment } from '../environments/environment';

import { PostListComponent } from './components/post-list/post-list.component';
import { PostCategoryMenuComponent } from './components/post-category-menu/post-category-menu.component';
import { UserRegistrationComponent } from './components/user-registration/user-registration.component';
import { UserLoginComponent } from './components/user-login/user-login.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { RequestResetComponent } from './components/request-reset/request-reset.component';
import { ResetResponseComponent } from './components/reset-response/reset-response.component';
import { PostDetailsComponent } from './components/post-details/post-details.component';
import { AddPostComponent } from './components/add-post/add-post.component';
import { UserPostComponent } from "./components/user-post/user-post.component";
import { InterestedBuyersComponent } from './components/interested-buyers/interested-buyers.component';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { VerifyUserComponent } from './components/verify-user/verify-user.component';
import { FontAwesomeModule, FaIconLibrary } from "@fortawesome/angular-fontawesome";
import { fas } from "@fortawesome/free-solid-svg-icons";
import { far } from "@fortawesome/free-regular-svg-icons";
import { fab } from "@fortawesome/free-brands-svg-icons";
import { InterestedPostListComponent } from './components/interested-post-list/interested-post-list.component';
import { EditPostComponent } from './components/edit-post/edit-post.component';

const routes: Routes = [
  { path: 'add-post', component: AddPostComponent },
  { path: 'edit-post', component: EditPostComponent },
  { path: 'category/:categoryId/:priceMin/:priceMax/:sortBy/:excellent/:lightUsage/:visibleUsage/:heavyUsage/:courseBook', component: PostListComponent },
  { path: 'category/:categoryId/:priceMin/:priceMax/:sortBy/:excellent/:lightUsage/:visibleUsage/:heavyUsage', component: PostListComponent },
  { path: 'category/:categoryId/:priceMin/:priceMax/:sortBy/:amenities/:furnished/:parking', component: PostListComponent },
  { path: 'search/:keyword/:priceMin/:priceMax/:sortBy/:excellent/:lightUsage/:visibleUsage/:heavyUsage', component: PostListComponent },
  { path: 'category', component: PostListComponent },
  { path: 'posts/:id', component: PostDetailsComponent },
  { path: 'post-details', component: PostDetailsComponent },
  { path: 'posts', component: PostListComponent },
  { path: 'forgot-password', component: RequestResetComponent },
  { path: 'reset-password', component: ResetResponseComponent },
  { path: 'registration', component: UserRegistrationComponent },
  { path: 'login', component: UserLoginComponent },
  { path: 'user-post', component: UserPostComponent },
  { path: 'post-buyers', component: InterestedBuyersComponent },
  { path: 'user-profile/interest-list', component: InterestedPostListComponent },
  { path: 'user-profile', component: UserProfileComponent },
  { path: 'edit-profile', component: EditProfileComponent },
  { path: 'change-password', component: ChangePasswordComponent },
  { path: 'verify-user', component: VerifyUserComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login', pathMatch: 'full' }
];

@NgModule({
  declarations: [
    AppComponent,
    PostListComponent,
    EditPostComponent,
    PostCategoryMenuComponent,
    UserRegistrationComponent,
    UserLoginComponent,
    RequestResetComponent,
    ResetResponseComponent,
    PostDetailsComponent,
    AddPostComponent,
    UserProfileComponent,
    UserPostComponent,
    InterestedBuyersComponent,
    AddPostComponent,
    EditProfileComponent,
    ChangePasswordComponent,
    NavbarComponent,
    VerifyUserComponent,
    InterestedPostListComponent

  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgbModule,
    AngularFireModule.initializeApp(environment.firebaseConfig),
    AngularFirestoreModule,
    FontAwesomeModule,
    BrowserAnimationsModule,
    FormsModule,
    ToastrModule.forRoot({
      closeButton: true,
      timeOut: 3000, // 3 seconds
      progressBar: true,
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(library: FaIconLibrary) {
    library.addIconPacks(fas, far, fab);
  }
}
