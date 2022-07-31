package ca.dal.marketplace.dao;

import ca.dal.marketplace.entity.UserSecurityToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins="*")
public interface UserSecurityTokenRepository extends JpaRepository<UserSecurityToken, Long> {
    UserSecurityToken findByToken(String token);
}
