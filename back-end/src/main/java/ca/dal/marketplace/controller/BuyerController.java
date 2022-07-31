package ca.dal.marketplace.controller;

import ca.dal.marketplace.dto.BuyerRequest;
import ca.dal.marketplace.entity.*;
import ca.dal.marketplace.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/buyer")
public class BuyerController {

    @Autowired
    private BuyerService buyerService;

    public BuyerController(BuyerService buyerService) {
        super();
        this.buyerService = buyerService;
    }

    @PostMapping(value = "/mark-as-interested")
    public ResponseEntity<String> markAsInterested(@RequestBody BuyerRequest buyerRequest) {

        HttpStatus responseStatus = HttpStatus.ACCEPTED;
        Map<String, Object> responseJson = new HashMap<>();

        buyerService.markAsInterested(buyerRequest);
        String responseMessage = "Added to Interested Buyer List!";

        responseJson.put("message", responseMessage);
        return new ResponseEntity(responseJson, responseStatus);
    }

    @PostMapping(value = "/accept-communication")
    public ResponseEntity<String> deletePost(@RequestBody Long buyerId) {

        HttpStatus responseStatus = HttpStatus.ACCEPTED;
        Map<String, Object> responseJson = new HashMap<>();

        buyerService.acceptCommunication(buyerId);
        String responseMessage = "Communication Accepted";

        responseJson.put("message", responseMessage);
        return new ResponseEntity(responseJson, responseStatus);
    }

    @PostMapping(value = "/get-interested-list")
    public Set<Buyer> getInterestedList(@RequestBody User user) {

        Set<Buyer> interestedList = buyerService.getInterestedList(user);
        resolveNestedJSON(interestedList);

        return interestedList;
    }

    // TODO: Resolve this JSON issue
    private void resolveNestedJSON(Set<Buyer> interestedList) {
        for (Buyer buyer : interestedList) {
            Post post = buyer.getPost();


            // Remove Category Nesting
            post.getCategory().setPosts(null);

            // Remove PostDetail Nesting
            for (PostDetail postDetail : post.getPostDetails()) {
                postDetail.setPost(null);
            }

            // Remove Media Nesting
            for (Media media : post.getMedia()) {
                media.setPost(null);
            }

            // Remove User Nesting
            User seller = post.getSeller();
            seller.setPosts(null);
            seller.setUserSecurityTokenList(null);

            post.setBuyers(null);
            buyer.setPost(post);

            User buyerSeller = buyer.getUser();;
            buyerSeller.setPosts(null);
            buyerSeller.setUserSecurityTokenList(null);
            buyer.setUser(buyerSeller);
        }
    }
}