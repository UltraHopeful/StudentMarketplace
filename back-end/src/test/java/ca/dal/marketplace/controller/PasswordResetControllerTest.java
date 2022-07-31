package ca.dal.marketplace.controller;

import ca.dal.marketplace.dao.UserRepository;
import ca.dal.marketplace.dao.UserSecurityTokenRepository;
import ca.dal.marketplace.entity.User;
import ca.dal.marketplace.service.MailService;
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
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;


import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PasswordResetControllerTest
{
    @Mock
    private MockEnvironment mockEnvironment;

    @InjectMocks
    private PasswordResetController passwordResetController;

    @Mock
    private BindingResult mockBindingResult;

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSecurityTokenRepository userSecurityTokenRepository;

    @Mock
    private MailService mailService;

    private User user;

    @BeforeEach
    void setUp()
    {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(passwordResetController).build();

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
    void tearDown() {
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void sendPasswordResetMail() throws Exception
    {
        String emailId = "abc@gmail.com";
        Mockito.lenient().when(mockBindingResult.hasErrors()).thenReturn(true);
        Mockito.when(mockEnvironment.getProperty("spring.url.home")).thenReturn("");
        Mockito.when(userRepository.findByEmailIgnoreCase(any())).thenReturn(user);
        Mockito.when(userSecurityTokenRepository.save(any())).thenReturn(null);
        Mockito.when(mailService.sendTokenInMail(any(), any(), any())).thenReturn(false);

        mockMvc.perform(get("/api/user/forgot-password")
                .param("emailId", emailId)
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    void validateResetToken() throws Exception
    {
        Mockito.lenient().when(userSecurityTokenRepository.findByToken(any())).thenReturn(null);
        Mockito.when(mockEnvironment.getProperty("spring.user.resetPasswordLinkExpiryTime")).thenReturn("5");

        mockMvc.perform(get("/api/user/confirm-reset")
                .param("token", "testtoken")
        ).andExpect(status().is2xxSuccessful());
    }


    @Test
    void updateUserPassword() throws Exception
    {
        Mockito.lenient().when(userSecurityTokenRepository.findByToken(any())).thenReturn(null);
        Mockito.when(mockEnvironment.getProperty("spring.user.resetPasswordLinkExpiryTime")).thenReturn("5");

        mockMvc.perform(post("/api/user/reset-password")
                .param("token", "testtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))).andDo(print()).andExpect(status().is2xxSuccessful());
    }
}