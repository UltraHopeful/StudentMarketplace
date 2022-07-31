package ca.dal.marketplace.service.impl;

import ca.dal.marketplace.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service("MailService")
public class MailServiceImpl implements MailService {

    @Autowired
    private Environment environment;

    @Autowired
    private Session mailSession;

    private boolean sendMail(MimeMessage mailMessage) {
        boolean isMailSend = false;

        try {
            MimeMessage mimeMessage = new MimeMessage(mailMessage);

            Transport.send(mimeMessage);

            isMailSend = true;
        }
        catch (MessagingException messagingE) {
            messagingE.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return isMailSend;
    }

    @Override
    public boolean sendTokenInMail(String userEmail,String emailSubject,String emailMessage) {
        boolean isMailSend = false;

        MimeMessage mailMessage = new MimeMessage(mailSession);

        String smtpMailUser = environment.getProperty("spring.mail.username");

        String homeUrl = environment.getProperty("spring.url.home");
        String userVerifyUrl = environment.getProperty("spring.url.verifyUser");

        try {
            InternetAddress from = new InternetAddress(smtpMailUser);
            InternetAddress to = new InternetAddress(userEmail);

            mailMessage.setFrom(from);
            mailMessage.addRecipient(Message.RecipientType.TO, to);
            mailMessage.setSubject(emailSubject);
            mailMessage.setText(emailMessage);

            isMailSend = sendMail(mailMessage);
        }
        catch (AddressException addressE){
            addressE.printStackTrace();
        }
        catch (MessagingException messagingE){
            messagingE.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return isMailSend;
    }
}
