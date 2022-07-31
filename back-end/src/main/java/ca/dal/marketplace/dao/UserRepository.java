package ca.dal.marketplace.dao;

import ca.dal.marketplace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin(origins="*")
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmailIgnoreCase(String email);

    Optional<User> findById(Long id);
}