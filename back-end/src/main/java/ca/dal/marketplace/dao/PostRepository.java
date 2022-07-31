package ca.dal.marketplace.dao;

import ca.dal.marketplace.entity.Category;
import ca.dal.marketplace.entity.Post;
import ca.dal.marketplace.entity.PostDetail;
import ca.dal.marketplace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Set;

@CrossOrigin(origins = "*")
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findPostsBySeller(User seller, Pageable pageable);

    Page<Post> findByTitleContainingAndPriceBetween(String keyword, Double priceMin, Double priceMax,
                                                    Pageable pageable);

    Page<Post> findByTitleContainingAndPriceBetweenAndPostDetailsIn(String keyword, Double priceMin,
                                                                    Double priceMax, Set<PostDetail> postDetails,
                                                                    Pageable pageable);

    Page<Post> findByCategoryAndPriceBetween(Category category, Double priceMin, Double priceMax,
                                             Pageable pageable);

    Page<Post> findByCategoryAndPriceBetweenAndPostDetailsIn(Category category, Double priceMin, Double priceMax,
                                                             Set<PostDetail> postDetails, Pageable pageable);
}