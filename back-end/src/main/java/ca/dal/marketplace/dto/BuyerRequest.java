package ca.dal.marketplace.dto;

import ca.dal.marketplace.entity.Post;
import ca.dal.marketplace.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BuyerRequest {
    private User user;
    private Post post;
}