package ca.dal.marketplace.dao;

import ca.dal.marketplace.entity.PostDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Set;

@CrossOrigin(origins="*")
public interface PostDetailRepository extends JpaRepository<PostDetail, Long> {

    Set<PostDetail> findPostDetailByFieldNameAndFieldValue(String fieldName, String fieldValue);

}