package ca.dal.marketplace.controller;

import ca.dal.marketplace.dao.PostRepository;
import ca.dal.marketplace.dto.CategoryPostsRequest;
import ca.dal.marketplace.dto.PostDto;
import ca.dal.marketplace.dto.SearchRequest;
import ca.dal.marketplace.dto.UserPostsRequest;
import ca.dal.marketplace.entity.*;
import ca.dal.marketplace.service.impl.PostDetailServiceImpl;
import ca.dal.marketplace.service.impl.PostServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static ca.dal.marketplace.controller.BuyerControllerTest.asJsonString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @InjectMocks
    private PostController postController;

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

    @Mock
    PostServiceImpl postService;

    @Mock
    PostDetailServiceImpl postDetailService;

    @Mock
    PostRepository postRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(postController).build();

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

    @AfterEach
    void tearDown()
    {

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addPost() throws Exception {

        PostDto postDto = new PostDto();
        postDto.setPost(post);

        Mockito.when(postService.addNewPost(any())).thenReturn(true);
        mockMvc.perform(post("/api/addPost")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(postDto))).andExpect(status().isCreated());
    }

    @Test
    void updatePost() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setPost(post);

        Mockito.when(postRepository.getById(any())).thenReturn(post);

         mockMvc.perform(post("/api/updatePost")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(postDto))).andExpect(status().is2xxSuccessful());
    }

    @Test
    void searchPosts() throws Exception {
//        Set<PostDetail> postDetails = new HashSet();
//        postDetails.add(postDetail);
//
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.setPostDetails(postDetails);
//
//        Mockito.when(postService.searchPost(searchRequest,false)).thenReturn(null);
//        Mockito.when(postDetailService.findPostDetailByFieldNameAndFieldValue(any(), any()).thenReturn('');
//        mockMvc.perform(post("/api/search")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(asJsonString(postDto))).andExpect(status().is2xxSuccessful());

    }

    @Test
    void categoryPosts() {

    }

    @Test
    void userPosts() throws Exception {

//        UserPostsRequest userPostsRequest = new UserPostsRequest();
//        userPostsRequest.setUser(user);
//
//        Mockito.when(postService.userPosts(any())).thenReturn(post);
//
//        mockMvc.perform(post("/api/user-posts")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(asJsonString(post))).andExpect(status().is2xxSuccessful());


    }

    @Test
    void deletePost() throws Exception {

        Mockito.when(postService.deletePostById(any())).thenReturn(true);

        mockMvc.perform(post("/api/user/delete-post")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(100l))).andExpect(status().is2xxSuccessful());

    }

    @Test
    void markPostAsSold() throws Exception {

        Mockito.when(postService.markPostAsSold(any())).thenReturn(true);

        mockMvc.perform(post("/api/post/mark-as-sold")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJsonString(100l))).andExpect(status().is2xxSuccessful());
    }
}