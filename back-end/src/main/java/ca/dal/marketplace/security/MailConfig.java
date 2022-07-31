package ca.dal.marketplace.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
public class MailConfig {

    private static final int SMTP_PORT = 587;

    @Autowired
    private Environment environment;

    @Bean
    public Session mailSession(){
        Session mailSession = null;

        Properties mailProperties = new Properties();

        mailProperties.put("mail.smtp.host","smtp.gmail.com");
        mailProperties.put("mail.smtp.auth",true);
        mailProperties.put("mail.smtp.starttls.enable",true);
        mailProperties.put("mail.smtp.port",SMTP_PORT);
        mailProperties.put("mail.smtp.ssl.protocols","TLSv1.2");

        String smtpMail = environment.getProperty("spring.mail.username");
        String smtpMailPassword = environment.getProperty("spring.mail.password");

        mailSession = Session.getInstance(mailProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpMail, smtpMailPassword);
            }
        });

        return mailSession;
    }
}
