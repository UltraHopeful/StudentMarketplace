import { Buyer } from "./buyer";
import { Category } from "./category";
import { Media } from "./media";
import { PostDetail } from "./post-detail";
import { User } from "./user";

export class Post {
    id: number;
    category: Category;
    seller: User;
    title: string;
    description: string;
    price: number;
    status: string;
    postDetails: PostDetail[];
    buyers: Buyer[];
    media: Media[];
    negotiable: string;
    markedInterested: boolean;
    acceptedCommunication: boolean;
    insertTimestamp: Date;
}
