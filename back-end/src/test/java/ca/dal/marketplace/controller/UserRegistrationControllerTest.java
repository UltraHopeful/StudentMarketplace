package ca.dal.marketplace.controller;

import ca.dal.marketplace.dao.UniversityDomainRepository;
import ca.dal.marketplace.dao.UserRepository;
import ca.dal.marketplace.dao.UserSecurityTokenRepository;
import ca.dal.marketplace.entity.User;
import ca.dal.marketplace.entity.UserSecurityToken;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserRegistrationControllerTest
{
    @Mock
    private UserSecurityTokenRepository userSecurityTokenRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private UniversityDomainRepository universityDomainRepository;

    @Mock
    private BindingResult mockBindingResult;

    @InjectMocks
    private UserRegistrationController userRegistrationController;

    @Mock
    private MockEnvironment mockEnvironment;

    private UserSecurityToken userSecurityToken;

    private User user;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp()
    {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userRegistrationController).build();

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

        userSecurityToken = new UserSecurityToken();
        userSecurityToken.setToken("testtoken");
        userSecurityToken.setCreationDateTime(date);
        userSecurityToken.setTokenId(1L);
    }

    @AfterEach
    void tearDown()
    {
        user=null;
        userSecurityToken=null;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerUser() throws Exception
    {
        Mockito.lenient().when(mockBindingResult.hasErrors()).thenReturn(true);
        Mockito.when(mockEnvironment.getProperty("spring.url.home")).thenReturn("");
        Mockito.when(universityDomainRepository.findByUniversityDomain(any())).thenReturn(null);
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))).andDo(print()).andExpect(status().is4xxClientError());
    }

    @Test
    void userConfirmation() throws Exception
    {
        Mockito.lenient().when(userSecurityTokenRepository.findByToken(any())).thenReturn(null);
        mockMvc.perform(post("/api/user/verify")
                        .param("token", "testtoken"))
                .andExpect(status().is2xxSuccessful());
    }
}