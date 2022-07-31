package ca.dal.marketplace.dto;

import ca.dal.marketplace.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserPostsRequest {
    private User user;
    private int pageNumber;
    private int pageSize;
}