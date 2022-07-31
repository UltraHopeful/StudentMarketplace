import { Category } from "./category";
import { PostDetail } from "./post-detail";

export class CategoryPost {
    category: Category;
    priceMin: number;
    priceMax: number;
    postDetails: PostDetail[];
    sortBy: number;
    pageNumber: number;
    pageSize: number;
}
