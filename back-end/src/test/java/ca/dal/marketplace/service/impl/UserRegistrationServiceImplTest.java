package ca.dal.marketplace.service.impl;

import ca.dal.marketplace.controller.PasswordResetController;
import ca.dal.marketplace.dao.UserRepository;
import ca.dal.marketplace.dto.PostDto;
import ca.dal.marketplace.dto.UserDto;
import ca.dal.marketplace.entity.Post;
import ca.dal.marketplace.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImplTest {

    @Mock
    UserRepository mockedUserRepository;

    @Mock
    User user;

    @InjectMocks
    private User userDto;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    UserRegistrationServiceImpl userRegistrationService= new UserRegistrationServiceImpl(mockedUserRepository);

    @BeforeEach
    @Temporal(TemporalType.DATE)
    void userDetails() throws ParseException {
        String sDate1="1998-01-29";
        Date date=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
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
        String encryptedPassword = passwordEncoder.encode(user.getPassword());

    }
    @Test
    void registerUser()  {
        when(mockedUserRepository.save(any(User.class))).thenReturn(user);
        User actualResult = userRegistrationService.registerUser(user);
        assertEquals(user,actualResult);
    }
}
