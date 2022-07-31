import { User } from "./user";

export class UserPostRequest {
    user: User;
    pageNumber: number;
    pageSize: number;
}
