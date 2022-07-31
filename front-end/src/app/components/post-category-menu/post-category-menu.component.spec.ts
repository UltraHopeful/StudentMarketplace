import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostCategoryMenuComponent } from './post-category-menu.component';

describe('PostCategoryMenuComponent', () => {
  let component: PostCategoryMenuComponent;
  let fixture: ComponentFixture<PostCategoryMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PostCategoryMenuComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PostCategoryMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
