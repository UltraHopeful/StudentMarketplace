package ca.dal.marketplace.controller;

import ca.dal.marketplace.dao.UserRepository;
import ca.dal.marketplace.dto.UserDto;
import ca.dal.marketplace.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserLoginControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserLoginController userLoginController;

    @Mock
    private MockEnvironment mockEnvironment;

    private MockMvc mockMvc;

    private User user;

    private UserDto userDto;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp()
    {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userLoginController).build();

        Date date=new Date();
        user = new User();
        user.setId(10L);
        user.setEmail("al581093@dal.ca");
        user.setPassword("alagu");
        user.setEnabled(true);
        user.setDateOfBirth(date);
        user.setFirstName("alagu");
        user.setLastName("karruppiah");
        user.setPhoneNumber("902-412-8559");
        user.setLocked(false);
        user.setFailedAttempts(0);
        String encryptedPassword ="@!#$%^&*";
        user.setPassword(encryptedPassword);
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
        Mockito.when(modelMapper.map(any(), any())).thenReturn(user);
        Mockito.when(userRepository.findByEmailIgnoreCase(any())).thenReturn(null);
        Mockito.when(mockEnvironment.getProperty(any())).thenReturn("10");

        mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))).andDo(print()).andExpect(status().is2xxSuccessful());
    }
}