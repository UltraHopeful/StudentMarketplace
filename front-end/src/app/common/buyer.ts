import { Post } from "./post";
import { User } from "./user";

export class Buyer {

    id: number;
    user: User;
    post: Post;
    canCommunicate: boolean;
}
