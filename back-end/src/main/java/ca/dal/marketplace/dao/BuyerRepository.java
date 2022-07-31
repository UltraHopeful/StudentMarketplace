package ca.dal.marketplace.dao;

import ca.dal.marketplace.entity.Buyer;
import ca.dal.marketplace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Set;

@CrossOrigin(origins = "*")
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

    Set<Buyer> findByUser(User user);
}