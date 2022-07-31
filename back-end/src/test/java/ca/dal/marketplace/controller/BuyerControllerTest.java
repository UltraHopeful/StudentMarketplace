package ca.dal.marketplace.controller;

import ca.dal.marketplace.dto.BuyerRequest;
import ca.dal.marketplace.entity.Buyer;
import ca.dal.marketplace.entity.Category;
import ca.dal.marketplace.entity.User;
import ca.dal.marketplace.service.BuyerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BuyerControllerTest {

    @Mock
    private BuyerService buyerService;

    @InjectMocks
    private BuyerController buyerController;

    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    void setUp()
    {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(buyerController).build();

        Date date=new Date();
        user = new User();
        user.setId(10L);
        user.setEmail("kv919136@dal.ca");
        user.setPassword("kavya");
        user.setEnabled(true);
        user.setDateOfBirth(date);
        user.setFirstName("kavya");
        user.setLastName("Kasaraneni");
        user.setPhoneNumber("902-412-8559");
        user.setLocked(false);
        user.setFailedAttempts(0);
        String encryptedPassword ="@!#$%^&*";
        user.setPassword(encryptedPassword);
    }

    @AfterEach
    void tearDown()
    {

    }

    @Test
    void markAsInterested() throws Exception
    {
        BuyerRequest buyerRequest = new BuyerRequest();
        buyerRequest.setUser(user);

        Mockito.when(buyerService.markAsInterested(any())).thenReturn(true);
        mockMvc.perform(post("/api/buyer/mark-as-interested")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(buyerRequest))).andDo(print()).andExpect(status().is2xxSuccessful());
    }

    @Test
    void deletePost() throws Exception
    {
        Mockito.when(buyerService.acceptCommunication(any())).thenReturn(true);
        mockMvc.perform(post("/api/buyer/accept-communication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(1L))).andDo(print()).andExpect(status().is2xxSuccessful());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getInterestedList() throws  Exception
    {
        Mockito.when(buyerService.getInterestedList(any())).thenReturn(new HashSet<Buyer>());

        mockMvc.perform(post("/api/buyer/get-interested-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))).andDo(print()).andExpect(status().is2xxSuccessful());
    }
}