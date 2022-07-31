package ca.dal.marketplace.service.impl;

import ca.dal.marketplace.service.MailService;
import org.apache.tomcat.util.buf.B2CConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {
//    @Mock
//    Environment environment;

    @Mock
    Session mailSession;

    @Mock
    MimeMessage mailMessage = new MimeMessage(mailSession);

    @InjectMocks
    MailServiceImpl mailService;

    String userEmail = "al581093@dal.ca";

    String emailSubject = "Student Market Place | Request to reset password sent. Check your inbox for the reset link.";

    String emailMessage = "To reset your account password please click link below :\n";
    Properties properties = new Properties();
// Setup mail server


    @BeforeEach
    void setUp() throws MessagingException {
        Address smtpMailUser = new InternetAddress("studentmarketplace2022@gmail.com");
        Address userEmailAddress = new InternetAddress("al581093@dal.ca");
        mailMessage.setFrom(smtpMailUser);
        mailMessage.addRecipient(Message.RecipientType.TO, userEmailAddress);
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port","587");

    }



//
//    @Test
//    void sendTokenInMail() {
//        boolean actualResult = mailService.sendTokenInMail(this.userEmail,this.emailSubject,this.emailMessage);
//        assertTrue(actualResult);
//
//    }
}