import { PostDetail } from "./post-detail";

export class SearchRequest {
    keyword: string;
    priceMin: number;
    priceMax: number;
    postDetails: PostDetail[];
    sortBy: number;
    pageNumber: number;
    pageSize: number;
}
