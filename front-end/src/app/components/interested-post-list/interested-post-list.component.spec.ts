import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InterestedPostListComponent } from './interested-post-list.component';

describe('InterestedPostListComponent', () => {
  let component: InterestedPostListComponent;
  let fixture: ComponentFixture<InterestedPostListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InterestedPostListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InterestedPostListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
