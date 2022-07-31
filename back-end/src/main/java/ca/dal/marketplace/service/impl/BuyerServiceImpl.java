package ca.dal.marketplace.service.impl;

import ca.dal.marketplace.dao.BuyerRepository;
import ca.dal.marketplace.dto.BuyerRequest;
import ca.dal.marketplace.entity.Buyer;
import ca.dal.marketplace.entity.User;
import ca.dal.marketplace.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private BuyerRepository buyerRepository;

    public BuyerServiceImpl(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    @Override
    public boolean markAsInterested(BuyerRequest buyerRequest) {

        Buyer buyer = new Buyer();
        buyer.setPost(buyerRequest.getPost());
        buyer.setUser(buyerRequest.getUser());
        buyer.setCanCommunicate(false);

        buyerRepository.save(buyer);
        return true;
    }

    @Override
    public boolean acceptCommunication(Long buyerId) {
        Buyer buyer = buyerRepository.getById(buyerId);
        buyer.setCanCommunicate(true);
        buyerRepository.save(buyer);
        return true;
    }

    @Override
    public Set<Buyer> getInterestedList(User user) {

        return buyerRepository.findByUser(user);
    }
}
