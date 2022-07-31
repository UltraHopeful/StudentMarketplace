import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Post} from '../common/post';
import {map} from 'rxjs/operators';
import {Category} from '../common/category';
import {PostDetail} from '../common/post-detail';
import {User} from '../common/user';
import {AddPost} from '../common/add-post';
import {environment} from "../../environments/environment";
import {SearchRequest} from '../common/search-request';
import {CategoryPost} from '../common/category-post';
import {UpdatePost} from "../common/update-post";

@Injectable({
    providedIn: 'root'
})
export class PostService {

    private environmentUrl: String = environment.baseUrl;

    private baseUrl = this.environmentUrl + 'api/posts';
    private searchUrl = this.environmentUrl + 'api/search';
    private categoryPostUrl = this.environmentUrl + 'api/category';
    private categoryUrl = this.environmentUrl + 'api/post-category';
    private addPostUrl = this.environmentUrl + 'api/addPost';
    private updatePostUrl = this.environmentUrl + 'api/updatePost';
    private deletePostUrl = this.environmentUrl + 'api/user/delete-post';
    private markAsSoldUrl = this.environmentUrl + 'api/post/mark-as-sold';

    constructor(private httpClient: HttpClient) {
    }

    getPost(thePostId: number): Observable<Post> {

        // need to build URL based on post id
        const postUrl = `${this.baseUrl}/${thePostId}`;
        return this.httpClient.get<Post>(postUrl);
    }

    getPostListPaginate(thePage: number,
                        thePageSize: number): Observable<GetResponsePosts> {

        // need to build URL based on page and size
        var allPostUrl = `${this.baseUrl}/?page=${thePage}&size=${thePageSize}`;
        return this.httpClient.get<GetResponsePosts>(allPostUrl);
    }

    getPostList(theCategoryId: number): Observable<Post[]> {

        // need to build URL based on category id
        var searchUrl = this.baseUrl;

        if (theCategoryId > 0) {
            searchUrl = `${this.baseUrl}/search/findByCategoryId?id=${theCategoryId}`;
        }

        return this.getPosts(searchUrl);
    }

    searchPosts(theKeyword: string): Observable<Post[]> {
        // need to build URL based on the keyword
        const searchUrl = `${this.baseUrl}/search/findByTitleContaining?searchText=${theKeyword}`;

        return this.getPosts(searchUrl);
    }

    searchPostsPaginate(searchRequest: SearchRequest): Observable<GetResponsePosts> {
        return this.httpClient.post<GetResponsePosts>(this.searchUrl, searchRequest);
    }

    categoryPostsPaginate(categoryPost: CategoryPost): Observable<GetResponsePosts> {

        // console.log(JSON.stringify(categoryPost));
        return this.httpClient.post<GetResponsePosts>(this.categoryPostUrl, categoryPost);
    }

    getPosts(searchUrl: string): Observable<Post[]> {
        return this.httpClient.get<GetResponsePosts>(searchUrl).pipe(map(response => response.content));
    }

    getPostCategories(): Observable<Category[]> {

        return this.httpClient.get<GetResponsePostCategory>(this.categoryUrl).pipe(
            map(response => response._embedded.postCategory)
        );
    }

    addPost(addPost: AddPost): Observable<any> {
        return this.httpClient.post<GetResponsePostMessage>(this.addPostUrl, addPost, {observe: "response"});
    }

    updatePost(updatePost: UpdatePost): Observable<any> {
        return this.httpClient.post<UpdatePost>(this.updatePostUrl, updatePost);
    }

    deletePost(postId: number): Observable<any> {
        return this.httpClient.post<GetResponsePostMessage>(this.deletePostUrl, postId, {observe: "response"});
    }

    markAsSold(postId: number): Observable<any> {
        return this.httpClient.post<GetResponsePostMessage>(this.markAsSoldUrl, postId, {observe: "response"});
    }
}

interface GetResponsePosts {
    content: Post[],
    totalElements: number,
    size: number,
    totalPages: number,
    number: number
}

interface GetResponsePostCategory {
    _embedded: {
        postCategory: Category[];
    }
}

interface GetResponsePostDetails {
    _embedded: {
        postDetails: PostDetail[];
    }
}

interface GetResponsePostSeller {
    _embedded: {
        user: User;
    }
}

interface GetResponsePostMessage {
    _embedded: {
        message: string;
    }
}
