package ca.dal.marketplace.controller;

import ca.dal.marketplace.dao.UserRepository;
import ca.dal.marketplace.dto.UserDto;
import ca.dal.marketplace.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.MockitoAnnotations.initMocks;

class UserProfileControllerTest {

    @InjectMocks
    private UserProfileController userProfileController;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    private MockMvc mockMvc;

    private User user;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userProfileController).build();
        Date date=new Date();
        user = new User();
        user.setId(10L);
        user.setEmail("al581093@dal.ca");
        user.setPassword("alagu");
        user.setEnabled(true);
        user.setDateOfBirth(date);
        user.setFirstName("alagu");
        user.setLastName("Karruppiah");
        user.setPhoneNumber("902-412-8559");
        user.setLocked(false);
        user.setFailedAttempts(0);
        String encryptedPassword ="@!#$%^&*";
        user.setPassword(encryptedPassword);

    }
        @Test
    void displayUserProfile() {

    }

    @Test
    void updateUserProfile() {
        UserDto userDto = new UserDto();


        Mockito.when(modelMapper.map(any(), any())).thenReturn(user);



    }

    @Test
    void changeUserPassword() {
    }

    @Test
    void deleteUserProfile() {
    }
}