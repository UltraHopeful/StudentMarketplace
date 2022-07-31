package ca.dal.marketplace.dao;

import ca.dal.marketplace.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins="*")
@RepositoryRestResource(collectionResourceRel = "postCategory", path = "post-category")
public interface CategoryRepository extends JpaRepository<Category, Long> {
}