package ca.dal.marketplace.service.impl;

import ca.dal.marketplace.dao.PostRepository;
import ca.dal.marketplace.dto.CategoryPostsRequest;
import ca.dal.marketplace.dto.PostDto;
import ca.dal.marketplace.dto.SearchRequest;
import ca.dal.marketplace.dto.UserPostsRequest;
import ca.dal.marketplace.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository mockedPostRepository;

    @InjectMocks
    private Post post;

    @InjectMocks
    private Category category;

    @InjectMocks
    private PostDto postDto;

    @InjectMocks
    private SearchRequest searchRequest;

    @InjectMocks
    private CategoryPostsRequest categoryPostsRequest;

    @InjectMocks
    private UserPostsRequest userPostsRequest;

    @InjectMocks
    private User user;

    @InjectMocks
    private PostDetail postDetail;

    @InjectMocks
    private Media media;

    @InjectMocks
    PostServiceImpl postService = new PostServiceImpl(mockedPostRepository);

    @BeforeEach
    void setup(){
        category.setId(1L);
        category.setCategoryName("Furniture");
        media.setLocation("Halifax");
        media.setPost(post);
        media.setMediaId(10L);
        user.setId(10L);
        user.setEmail("al581093@dal.ca");
        postDetail.setPostDetailId(781L);
        postDetail.setFieldName("Coursebook");
        postDetail.setFieldValue("Yes");
        Set<PostDetail> postDetails = new HashSet();
        Set<Media> medias = new HashSet();
        postDetails.add(postDetail);
        medias.add(media);
        post.setId((long) 501);
        post.setTitle("Title1");
        post.setDescription("Dec1");
        post.setPrice(211.0);
        post.setCategory(category);
        post.setSeller(user);
        post.setStatus("open");
        postDto.setPost(post);
        postDto.setPostDetails(postDetails);
        postDto.setMedia(medias);
        searchRequest.setPostDetails(postDetails);
        searchRequest.setKeyword("books");
        searchRequest.setPriceMin(12.0);
        searchRequest.setPriceMax(14.0);
        searchRequest.setPageNumber(2);
        searchRequest.setPageSize(3);
        categoryPostsRequest.setCategory(category);
        categoryPostsRequest.setPostDetails(postDetails);
        categoryPostsRequest.setPriceMin(11.0);
        categoryPostsRequest.setPriceMax(12.0);
        categoryPostsRequest.setPageNumber(1);
        categoryPostsRequest.setPageSize(2);
        userPostsRequest.setUser(user);
        userPostsRequest.setPageNumber(2);
        userPostsRequest.setPageSize(1);
    }
    @Test
    public void addNewPost() {
        when(mockedPostRepository.save(any(Post.class))).thenReturn(post);
        boolean actualResult = postService.addNewPost(postDto);
        assertTrue(actualResult);
    }


    @Test
    void searchPost() {
        Page postPage = postService.searchPost(searchRequest,true);
        Page postPageWithoutPostDetails = postService.searchPost(searchRequest,false);
        Pageable pageable = Pageable.ofSize(searchRequest.getPageSize()).withPage(searchRequest.getPageNumber());
        assertEquals(mockedPostRepository.findByTitleContainingAndPriceBetweenAndPostDetailsIn(searchRequest.getKeyword(),searchRequest.getPriceMax(),searchRequest.getPriceMax(),searchRequest.getPostDetails(),pageable),postPage);
        assertEquals(mockedPostRepository.findByTitleContainingAndPriceBetween(searchRequest.getKeyword(),searchRequest.getPriceMax(),searchRequest.getPriceMax(),pageable),postPageWithoutPostDetails);
    }

    @Test
    void categoryPosts() {
        Page postPage = postService.categoryPosts(categoryPostsRequest,true);
        Page postPageWithoutPostDetails = postService.categoryPosts(categoryPostsRequest,false);
        Pageable pageable = Pageable.ofSize(categoryPostsRequest.getPageSize()).withPage(categoryPostsRequest.getPageNumber());
        assertEquals(mockedPostRepository.findByCategoryAndPriceBetweenAndPostDetailsIn(categoryPostsRequest.getCategory(),categoryPostsRequest.getPriceMax(),categoryPostsRequest.getPriceMax(),categoryPostsRequest.getPostDetails(),pageable),postPage);
        assertEquals(mockedPostRepository.findByCategoryAndPriceBetween(categoryPostsRequest.getCategory(),categoryPostsRequest.getPriceMax(),categoryPostsRequest.getPriceMax(),pageable),postPageWithoutPostDetails);
    }

    @Test
    void userPosts(){
        Pageable pageable = Pageable.ofSize(userPostsRequest.getPageSize()).withPage(userPostsRequest.getPageNumber());
        Page postPage = postService.userPosts(userPostsRequest);
        assertEquals(mockedPostRepository.findPostsBySeller(userPostsRequest.getUser(),pageable),postPage);
    }

    @Test
    void deletePostById() {
        boolean deletePost = postService.deletePostById(post.getId());
        assertTrue(deletePost);
    }

    @Test
    void markPostAsSold() {
        Mockito.when(mockedPostRepository.getById(any())).thenReturn(post);
        boolean markAsSold = postService.markPostAsSold(post.getId());
        assertEquals(true,markAsSold);
    }
}