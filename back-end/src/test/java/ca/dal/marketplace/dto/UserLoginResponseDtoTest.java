package ca.dal.marketplace.dto;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginResponseDtoTest {

    @InjectMocks
    UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto();

    @Test
    void getId() {
        long id = 10L;
        userLoginResponseDto.setId(10L);
        long expectedId = userLoginResponseDto.getId();
        assertEquals(expectedId,id);
    }

    @Test
    void getFirstName() {
        String firstName = "AlaguSwrnam";
        userLoginResponseDto.setFirstName("AlaguSwrnam");
        String expectedFirstName = userLoginResponseDto.getFirstName();
        assertEquals(expectedFirstName,firstName);
    }

    @Test
    void getEmail() {
        String email = "al581093@dal.ca";
        userLoginResponseDto.setEmail("al581093@dal.ca");
        String expectedEmail = userLoginResponseDto.getEmail();
        assertEquals(expectedEmail,email);
    }

    @Test
    void setId() {
        long id = 10L;
        userLoginResponseDto.setId(id);
        assertEquals(userLoginResponseDto.getId(),id);
    }

    @Test
    void setFirstName() {
        String firstName = "AlaguSwrnam";
        userLoginResponseDto.setFirstName(firstName);
        assertEquals(userLoginResponseDto.getFirstName(),firstName);
    }

    @Test
    void setEmail() {
        String email = "al581093@dal.ca";
        userLoginResponseDto.setEmail(email);
        assertEquals(userLoginResponseDto.getEmail(),email);
    }
}