package ca.dal.marketplace.dto;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import javax.xml.crypto.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @InjectMocks
    UserDto userDto = new UserDto();

    @Test
    void getId() {
        long id = 10L;
        userDto.setId(10L);
        long expectedId = userDto.getId();
        assertEquals(expectedId,id);
    }

    @Test
    void getFirstName() {
        String firstName = "AlaguSwrnam";
        userDto.setFirstName("AlaguSwrnam");
        String expectedFirstName = userDto.getFirstName();
        assertEquals(expectedFirstName,firstName);
    }

    @Test
    void getLastName() {
        String lastName = "Karruppiah";
        userDto.setLastName("Karruppiah");
        String expectedLastName = userDto.getLastName();
        assertEquals(expectedLastName,lastName);

    }

    @Test
    void getEmail() {
        String email = "al581093@dal.ca";
        userDto.setEmail("al581093@dal.ca");
        String expectedEmail = userDto.getEmail();
        assertEquals(expectedEmail,email);
    }

    @Test
    void getPassword() {
        String password = "hello123";
        userDto.setPassword("hello123");
        String expectedPassword = userDto.getPassword();
        assertEquals(expectedPassword,password);
    }

    @Test
    void getPhoneNumber() {
        String phoneNumber = "902-413-9876";
        userDto.setPhoneNumber("902-413-9876");
        String expectedPhoneNumber = userDto.getPhoneNumber();
        assertEquals(expectedPhoneNumber,phoneNumber);
    }

    @Test
    void getDateOfBirth() throws ParseException {
        String sDate1="1998-01-29";
        Date date=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        userDto.setDateOfBirth(date);
        Date expectedDate = userDto.getDateOfBirth();
        assertEquals(expectedDate,date);
    }

    @Test
    void getFailedAttempts() {
        int attempts = 1;
        userDto.setFailedAttempts(1);
        int expectedAttempts = userDto.getFailedAttempts();
        assertEquals(expectedAttempts,attempts);
    }

    @Test
    void setId() {
        long id = 10L;
        userDto.setId(id);
        assertEquals(userDto.getId(),id);
    }

    @Test
    void setFirstName() {
        String firstName = "AlaguSwrnam";
        userDto.setFirstName(firstName);
        assertEquals(userDto.getFirstName(),firstName);
    }

    @Test
    void setLastName() {
        String lastName = "Karruppiah";
        userDto.setFirstName(lastName);
        assertEquals(userDto.getFirstName(),lastName);
    }

    @Test
    void setEmail() {
        String email = "al581093@dal.ca";
        userDto.setEmail(email);
        assertEquals(userDto.getEmail(),email);
    }

    @Test
    void setPassword() {
        String password = "hello123";
        userDto.setPassword(password);
        assertEquals(userDto.getPassword(),password);
    }

    @Test
    void setPhoneNumber() {
        String phoneNumber = "902-413-9876";
        userDto.setPhoneNumber(phoneNumber);
        assertEquals(userDto.getPhoneNumber(),phoneNumber);
    }

    @Test
    void setDateOfBirth() throws ParseException {
        String sDate1="1998-01-29";
        Date date=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        userDto.setDateOfBirth(date);
        assertEquals(userDto.getDateOfBirth(),date);

    }
}