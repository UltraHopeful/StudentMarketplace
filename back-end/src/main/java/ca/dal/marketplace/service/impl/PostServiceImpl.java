package ca.dal.marketplace.service.impl;

import ca.dal.marketplace.dao.PostRepository;
import ca.dal.marketplace.dto.CategoryPostsRequest;
import ca.dal.marketplace.dto.PostDto;
import ca.dal.marketplace.dto.SearchRequest;
import ca.dal.marketplace.dto.UserPostsRequest;
import ca.dal.marketplace.entity.Media;
import ca.dal.marketplace.entity.Post;
import ca.dal.marketplace.entity.PostDetail;
import ca.dal.marketplace.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {

    private static final int PRICE_LOW_TO_HIGH = 1;
    private static final int PRICE_HIGH_TO_LOW = 2;

    @Autowired
    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public boolean addNewPost(PostDto postDto) {

        Post post = postDto.getPost();
        Set<PostDetail> allPostDetails = postDto.getPostDetails();
        Set<Media> allMedia = postDto.getMedia();

        for (PostDetail pd : allPostDetails) {
            PostDetail postDetail = new PostDetail();
            postDetail.setFieldName(pd.getFieldName());
            postDetail.setFieldValue(pd.getFieldValue());
            post.add(postDetail);
        }

        for (Media md : allMedia) {
            Media media = new Media();
            media.setLocation(md.getLocation());
            post.add(media);
        }
        postRepository.save(post);
        return true;
    }

    @Override
    public Page<Post> searchPost(SearchRequest searchRequest, boolean hasPostDetails) {

        Sort sort = Sort.by("insertTimestamp").descending();
        if (searchRequest.getSortBy() == PRICE_LOW_TO_HIGH) { // Price: Low to High
            sort = Sort.by("price").ascending();
        } else if (searchRequest.getSortBy() == PRICE_HIGH_TO_LOW) { // Price: High to Low
            sort = Sort.by("price").descending();
        }
        Pageable pageable = PageRequest.of(searchRequest.getPageNumber(), searchRequest.getPageSize(), sort);
        if (searchRequest.getPriceMax() < searchRequest.getPriceMin()) {
            searchRequest.setPriceMax(Double.MAX_VALUE);
        }

        if (hasPostDetails) {
            return postRepository.findByTitleContainingAndPriceBetweenAndPostDetailsIn(
                    searchRequest.getKeyword(),
                    searchRequest.getPriceMin(),
                    searchRequest.getPriceMax(),
                    searchRequest.getPostDetails(),
                    pageable);
        } else {
            return postRepository.findByTitleContainingAndPriceBetween(
                    searchRequest.getKeyword(),
                    searchRequest.getPriceMin(),
                    searchRequest.getPriceMax(),
                    pageable);
        }
    }

    @Override
    public Page<Post> categoryPosts(CategoryPostsRequest categoryPostsRequest, boolean hasPostDetails) {

        Sort sort = Sort.by("insertTimestamp").descending();
        if (categoryPostsRequest.getSortBy() == PRICE_LOW_TO_HIGH) { // Price: Low to High
            sort = Sort.by("price").ascending();
        } else if (categoryPostsRequest.getSortBy() == PRICE_HIGH_TO_LOW) { // Price: High to Low
            sort = Sort.by("price").descending();
        }
        Pageable pageable = PageRequest.of(categoryPostsRequest.getPageNumber(), categoryPostsRequest.getPageSize(), sort);
        if (categoryPostsRequest.getPriceMax() < categoryPostsRequest.getPriceMin()) {
            categoryPostsRequest.setPriceMax(Double.MAX_VALUE);
        }

        if (hasPostDetails) {
            return postRepository.findByCategoryAndPriceBetweenAndPostDetailsIn(
                    categoryPostsRequest.getCategory(),
                    categoryPostsRequest.getPriceMin(),
                    categoryPostsRequest.getPriceMax(),
                    categoryPostsRequest.getPostDetails(),
                    pageable);
        } else {
            return postRepository.findByCategoryAndPriceBetween(
                    categoryPostsRequest.getCategory(),
                    categoryPostsRequest.getPriceMin(),
                    categoryPostsRequest.getPriceMax(),
                    pageable);
        }
    }

    @Override
    public Page<Post> userPosts(UserPostsRequest userPostsRequest) {

        Pageable pageable = PageRequest.of(userPostsRequest.getPageNumber(), userPostsRequest.getPageSize(),
                Sort.by("insertTimestamp").descending());

        return postRepository.findPostsBySeller(userPostsRequest.getUser(), pageable);
    }

    @Override
    public boolean deletePostById(Long postId) {
        postRepository.deleteById(postId);
        return true;
    }

    @Override
    public boolean markPostAsSold(Long postId) {

        Post post = postRepository.getById(postId);
        post.setStatus("Sold");
        post.setClosedTimestamp(new Timestamp(System.currentTimeMillis()));
        postRepository.save(post);
        return true;
    }
}
