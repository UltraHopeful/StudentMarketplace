package ca.dal.marketplace.dto;

import ca.dal.marketplace.entity.Category;
import ca.dal.marketplace.entity.PostDetail;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
@Getter
@Setter
public class CategoryPostsRequest {
    private Category category;
    private Double priceMin;
    private Double priceMax;
    private Set<PostDetail> postDetails;
    private int sortBy;
    private int pageNumber;
    private int pageSize;
}