package ca.dal.marketplace.controller;

import ca.dal.marketplace.dao.PostRepository;
import ca.dal.marketplace.dto.CategoryPostsRequest;
import ca.dal.marketplace.dto.PostDto;
import ca.dal.marketplace.dto.SearchRequest;
import ca.dal.marketplace.dto.UserPostsRequest;
import ca.dal.marketplace.entity.*;
import ca.dal.marketplace.service.PostDetailService;
import ca.dal.marketplace.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private ModelMapper modelMapper;

    private PostService postService;
    private PostDetailService postDetailService;

    @Autowired
    private PostRepository postRepository;

    public PostController(PostService postService, PostDetailService postDetailService) {
        super();
        this.postService = postService;
        this.postDetailService = postDetailService;
    }

    @PostMapping(value = "/addPost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addPost(@RequestBody PostDto postDto) {

        HttpStatus responseStatus = HttpStatus.ACCEPTED;
        Map<String, Object> responseJson = new HashMap<>();

        postService.addNewPost(postDto);

        String responseMessage = "Post Added!";
        responseJson.put("message", responseMessage);
        return new ResponseEntity(responseJson, HttpStatus.CREATED);
    }

    @PostMapping(value = "/updatePost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto) {

        HttpStatus responseStatus = HttpStatus.ACCEPTED;
        Map<String, Object> responseJson = new HashMap<>();

        Long postId = postDto.getPost().getId();
//        Post postToUpdate = modelMapper.map(postDto,Post.class);

        Post postToUpdate = postRepository.getById(postId);
        postToUpdate.setPrice(postDto.getPost().getPrice());
        postToUpdate.setTitle(postDto.getPost().getTitle());
        postToUpdate.setDescription(postDto.getPost().getDescription());
        postToUpdate.setNegotiable(postDto.getPost().getNegotiable());
//        Post postToUpdate = postDto.getPost();

        postRepository.save(postToUpdate);
        responseStatus = HttpStatus.OK;
        responseJson.put("message", "post updated successfully");
        return new ResponseEntity(responseJson, responseStatus);
    }

    @PostMapping(value = "/search")
    public Page<Post> searchPosts(@RequestBody SearchRequest searchRequest) {

        boolean hasPostDetails = false;
        if (searchRequest.getPostDetails() != null && searchRequest.getPostDetails().size() > 0) {
            hasPostDetails = true;
            searchRequest.setPostDetails(getPostDetailList(searchRequest.getPostDetails()));
        }

        Page<Post> posts = postService.searchPost(searchRequest, hasPostDetails);

        resolveNestedJSON(posts);

        return posts;
    }

    @PostMapping(value = "/category")
    public Page<Post> categoryPosts(@RequestBody CategoryPostsRequest categoryPostsRequest) {

        boolean hasPostDetails = false;
        if (categoryPostsRequest.getPostDetails() != null && categoryPostsRequest.getPostDetails().size() > 0) {
            hasPostDetails = true;
            categoryPostsRequest.setPostDetails(getPostDetailList(categoryPostsRequest.getPostDetails()));
        }

        Page<Post> posts = postService.categoryPosts(categoryPostsRequest, hasPostDetails);

        resolveNestedJSON(posts);

        return posts;
    }

    @PostMapping(value = "/user-posts")
    public Page<Post> userPosts(@RequestBody UserPostsRequest userPostsRequest) {

        Page<Post> posts = postService.userPosts(userPostsRequest);
        resolveNestedJSON(posts);

        return posts;
    }

    @PostMapping(value = "/user/delete-post")
    public ResponseEntity<String> deletePost(@RequestBody Long postId) {

        HttpStatus responseStatus = HttpStatus.ACCEPTED;
        Map<String, Object> responseJson = new HashMap<>();

        postService.deletePostById(postId);
        String responseMessage = "Post Deleted";

        responseJson.put("message", responseMessage);
        return new ResponseEntity(responseJson, responseStatus);
    }

    @PostMapping(value = "/post/mark-as-sold")
    public ResponseEntity<String> markPostAsSold(@RequestBody Long postId) {

        HttpStatus responseStatus = HttpStatus.ACCEPTED;
        Map<String, Object> responseJson = new HashMap<>();

        postService.markPostAsSold(postId);
        String responseMessage = "Post Marked as sold";

        responseJson.put("message", responseMessage);
        return new ResponseEntity(responseJson, responseStatus);
    }

    // TODO: Resolve this issue
    private void resolveNestedJSON(Page<Post> posts) {
        for (Post post : posts.toList()) {

            // Remove Category Nesting
            Category category = post.getCategory();
            category.setPosts(null);

            // Remove PostDetail Nesting
            for (PostDetail postDetail : post.getPostDetails()) {
                postDetail.setPost(null);
            }

            // Remove Media Nesting
            for (Media media : post.getMedia()) {
                media.setPost(null);
            }

            // Remove User Nesting
            User seller = post.getSeller();
            seller.setPosts(null);
            seller.setUserSecurityTokenList(null);

            // Remove Buyer Nesting
            for (Buyer buyer : post.getBuyers()) {
                buyer.setPost(null);
                buyer.getUser().setPosts(null);
                buyer.getUser().setUserSecurityTokenList(null);
            }
        }
    }

    private Set<PostDetail> getPostDetailList(Set<PostDetail> postDetailSet) {

        Set<PostDetail> conditionPostDetails = new HashSet<>();
        Set<PostDetail> conditionPostDetailsUnion = new HashSet<>();
        Set<PostDetail> otherPostDetails = new HashSet<>();
        Set<PostDetail> otherPostDetailsUnion = new HashSet<>();

        boolean hasConditionDetails = false;
        boolean hasOtherDetails = false;

        // Segregating details for union and intersection
        for (PostDetail postDetail : postDetailSet) {
            if (postDetail.getFieldName().equals("condition")) {
                conditionPostDetails.add(postDetail);
                hasConditionDetails = true;
            } else {
                otherPostDetails.add(postDetail);
                hasOtherDetails = true;
            }
        }

        // Condition post details
        boolean isFirstCondition = true;
        for (PostDetail postDetail : conditionPostDetails) {
            if (isFirstCondition) {
                conditionPostDetailsUnion = postDetailService.findPostDetailByFieldNameAndFieldValue(
                        postDetail.getFieldName()
                        , postDetail.getFieldValue());
                isFirstCondition = false;
                continue;
            }

            Set<PostDetail> postDetailsCondition = postDetailService.findPostDetailByFieldNameAndFieldValue(
                    postDetail.getFieldName()
                    , postDetail.getFieldValue());

            conditionPostDetailsUnion = getPostDetailUnionSet(conditionPostDetailsUnion, postDetailsCondition);
        }

        // Other post details
        boolean isFirstOtherDetails = true;
        for (PostDetail postDetail : otherPostDetails) {
            if (isFirstOtherDetails) {
                otherPostDetailsUnion = postDetailService.findPostDetailByFieldNameAndFieldValue(
                        postDetail.getFieldName()
                        , postDetail.getFieldValue());
                isFirstOtherDetails = false;
                continue;
            }

            Set<PostDetail> postDetailsOther = postDetailService.findPostDetailByFieldNameAndFieldValue(
                    postDetail.getFieldName()
                    , postDetail.getFieldValue());

            otherPostDetailsUnion = getPostDetailIntersectionSet(otherPostDetailsUnion, postDetailsOther);
        }

        if (hasConditionDetails && hasOtherDetails) {
            return getPostDetailIntersectionSet(conditionPostDetailsUnion, otherPostDetailsUnion);
        } else if (hasConditionDetails) {
            return conditionPostDetailsUnion;
        } else {
            return otherPostDetailsUnion;
        }
    }

    private Set<PostDetail> getPostDetailIntersectionSet(Set<PostDetail> postDetailSet1, Set<PostDetail> postDetailSet2) {

        Set<PostDetail> postDetailIntersection = new HashSet<>();

        for (PostDetail postDetail1 : postDetailSet1) {
            innerLoop:
            for (PostDetail postDetail2 : postDetailSet2) {
                if (postDetail1.getPost().getId() == postDetail2.getPost().getId()) {
                    postDetailIntersection.add(postDetail1);
                    break innerLoop;
                }
            }
        }

        return postDetailIntersection;
    }

    private Set<PostDetail> getPostDetailUnionSet(Set<PostDetail> postDetailSet1, Set<PostDetail> postDetailSet2) {

        Set<PostDetail> postDetailUnion = postDetailSet1;

        for (PostDetail postDetail2 : postDetailSet2) {
            boolean foundObject = false;
            innerLoop:
            for (PostDetail postDetail1 : postDetailSet1) {
                if (postDetail1.getPostDetailId() == postDetail2.getPostDetailId()) {
                    foundObject = true;
                    break innerLoop;
                }
            }
            if (!foundObject) {
                postDetailUnion.add(postDetail2);
            }
        }

        return postDetailUnion;
    }
}