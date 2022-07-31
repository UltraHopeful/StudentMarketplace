package ca.dal.marketplace.service.impl;

import ca.dal.marketplace.dao.BuyerRepository;
import ca.dal.marketplace.dto.BuyerRequest;
import ca.dal.marketplace.entity.Buyer;
import ca.dal.marketplace.entity.Category;
import ca.dal.marketplace.entity.Post;
import ca.dal.marketplace.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuyerServiceImplTest {

    @Mock
    private BuyerRepository buyerRepository;

    @InjectMocks
    private User user;

    @InjectMocks
    private Post post;

    @InjectMocks
    private Category category;

    @InjectMocks
    private Buyer buyer;

    @InjectMocks
    private BuyerServiceImpl buyerService = new BuyerServiceImpl(buyerRepository);

    @InjectMocks
    private BuyerRequest buyerRequest ;

    @BeforeEach
    void setUp(){
        user.setId(10L);
        user.setEmail("al581093@dal.ca");
        category.setId(1L);
        category.setCategoryName("Furniture");
        post.setId((long) 501);
        post.setTitle("Title1");
        post.setDescription("Dec1");
        post.setPrice(211.0);
        post.setCategory(category);
        post.setSeller(user);
        post.setStatus("open");
        buyer.setId((long)100);
        buyer.setUser(user);
        buyer.setPost(post);
        buyer.setCanCommunicate(false);
        buyerRequest.setUser(user);
        buyerRequest.setPost(post);

    }

    @Test
    void markAsInterested() {
        when(buyerRepository.save(any(Buyer.class))).thenReturn(buyer);
        boolean actualResult = buyerService.markAsInterested(new BuyerRequest());
        assertTrue(actualResult);
    }

    @Test
    void acceptCommunication() {
        Mockito.when(buyerRepository.getById(any())).thenReturn(buyer);
        boolean acceptCommunication = buyerService.acceptCommunication(buyer.getId());
        assertEquals(true,acceptCommunication);
    }

    @Test
    void getInterestedList() {
        Set<Buyer> buyer = buyerService.getInterestedList(user);
        assertEquals(buyerRepository.findByUser(user),buyer);
    }
}