package ca.dal.marketplace.service;

import ca.dal.marketplace.dto.BuyerRequest;
import ca.dal.marketplace.entity.Buyer;
import ca.dal.marketplace.entity.User;

import java.util.Set;

public interface BuyerService {

    boolean markAsInterested(BuyerRequest buyerRequest);

    boolean acceptCommunication(Long buyerId);

    Set<Buyer> getInterestedList(User user);
}