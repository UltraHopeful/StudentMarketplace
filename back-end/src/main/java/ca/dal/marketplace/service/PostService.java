package ca.dal.marketplace.service;

import ca.dal.marketplace.dto.*;
import ca.dal.marketplace.entity.Post;
import org.springframework.data.domain.Page;

public interface PostService {

    boolean addNewPost(PostDto postDto);

    Page<Post> searchPost(SearchRequest searchRequest, boolean hasPostDetails);

    Page<Post> categoryPosts(CategoryPostsRequest categoryPostsRequest, boolean hasPostDetails);

    Page<Post> userPosts(UserPostsRequest userPostsRequest);

    boolean deletePostById(Long postId);

    boolean markPostAsSold(Long postId);
}