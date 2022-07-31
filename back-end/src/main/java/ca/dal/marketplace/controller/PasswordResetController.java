package ca.dal.marketplace.controller;

import ca.dal.marketplace.dao.UserRepository;
import ca.dal.marketplace.dao.UserSecurityTokenRepository;
import ca.dal.marketplace.entity.User;
import ca.dal.marketplace.entity.UserSecurityToken;
import ca.dal.marketplace.service.MailService;
import ca.dal.marketplace.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/user")
public class PasswordResetController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private UserSecurityTokenRepository userSecurityTokenRepository;

    @Autowired
    private Environment environment;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public PasswordResetController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    /**
     * Receive email of the user, create token and send it via email to the user
     */
    @GetMapping(value = "/forgot-password",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendPasswordResetMail(@RequestParam("emailId") @Valid String email) {
        String responseMessage = "";

        HttpStatus responseStatus = HttpStatus.ACCEPTED;

        Map<String, String> responseJson = new HashMap<>();

        String homeUrl = environment.getProperty("spring.url.home");
        String resetPasswordUrl = environment.getProperty("spring.url.resetPasswordUrl");

        String emailSubject = "Student Market Place | Request to reset password sent. Check your inbox for the reset link.";
        String emailMessage = "To reset your account password please click link below :\n";

        User userExists = userRepository.findByEmailIgnoreCase(email);

        if (userExists != null) {

            // getting email of user for user object
            String userEmail = email;

            // getting password reset object with token
            UserSecurityToken userSecurityToken = new UserSecurityToken(userExists);

            userSecurityTokenRepository.save(userSecurityToken);

            emailMessage += homeUrl + resetPasswordUrl + "?token=" + userSecurityToken.getToken();

            // checking the mail is send or not
            boolean isMailSend = mailService.sendTokenInMail(userEmail, emailSubject, emailMessage);

            // if mail is send then we add user and account verification details in the database
            if (isMailSend) {
                userRegistrationService.registerUser(userExists );
                this.userSecurityTokenRepository.save(userSecurityToken);
                responseStatus = HttpStatus.OK;
                responseMessage = "Please check your mail " + userEmail + " for password reset link";
            } else {
                responseStatus = HttpStatus.ACCEPTED;
                responseMessage = "Some error sending mail. So can you try some time after";
            }

        }
        else{
            responseStatus = HttpStatus.NOT_ACCEPTABLE;
            responseMessage = "Sorry user email doesn't exists";
        }

        responseJson.put("message", responseMessage);
        return new ResponseEntity(responseJson, responseStatus);
    }

    /**
     * Receive the token from the link sent via email and reset the password for the user
     */

    @GetMapping(value = "/confirm-reset",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validateResetToken(@RequestParam("token")String authToken){
        UserSecurityToken receivedVerificationToken = userSecurityTokenRepository.findByToken(authToken);

        String responseMessage = "";

        HttpStatus responseStatus = HttpStatus.ACCEPTED;

        int resetLinkExpiryTime = Integer.parseInt(environment.getProperty("spring.user.resetPasswordLinkExpiryTime"));

        Map<String,String> responseJson = new HashMap<>();
        Date currentTime = new Date();

        // token validation
        if(receivedVerificationToken != null)
        {
            long receivedTokenCalculateTime = currentTime.getTime()-receivedVerificationToken.getCreationDateTime().getTime();
            if(receivedTokenCalculateTime<resetLinkExpiryTime) {
                responseStatus = HttpStatus.OK;
                responseJson.put("userMail",receivedVerificationToken.getUser().getEmail());
                responseMessage = "Valid Token you can reset your password now";
            }
            else{
                responseStatus = HttpStatus.ACCEPTED;
                responseMessage="You are not allowed to reset the password because link is expired";
            }
        }
        // if the token is not verified,so it is broken or invalid link
        else{
            responseStatus = HttpStatus.NON_AUTHORITATIVE_INFORMATION;
            responseMessage="You are not allowed to reset the password because link is broken";
        }

        responseJson.put("message",responseMessage);

        return new ResponseEntity(responseJson,responseStatus);
    }

    @PostMapping(value = "/reset-password",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserPassword(@RequestParam("token")String authToken,@RequestBody @Valid User user){

        String responseMessage = "";

        HttpStatus responseStatus = HttpStatus.ACCEPTED;

        int resetLinkExpiryTime = Integer.parseInt(environment.getProperty("spring.user.resetPasswordLinkExpiryTime"));

        Map<String,String> responseJson = new HashMap<>();

        UserSecurityToken receivedVerificationToken = userSecurityTokenRepository.findByToken(authToken);
        Date currentTime = new Date();

        // token validation and find user by email
        if(receivedVerificationToken != null){
            long receivedTokenCalculateTime = currentTime.getTime()-receivedVerificationToken.getCreationDateTime().getTime();

            if(receivedTokenCalculateTime<resetLinkExpiryTime) {

                if (user.getEmail() != null) {
                    User tokenUser = userRepository.findByEmailIgnoreCase(user.getEmail());
                    tokenUser.setEnabled(true);
                    tokenUser.setPassword(encoder.encode(user.getPassword()));
                    tokenUser.setLocked(false);
                    userRepository.save(tokenUser);
                    responseStatus = HttpStatus.OK;
                    responseMessage = "Password successfully reset. You can now log in with the new credentials.";
                }
            }
            else{
                responseStatus = HttpStatus.ACCEPTED;
                responseMessage = "Broken link is expired you have to generate it again";
            }
        }
        // if the link is not verified,so it is broken or invalid link
        else{
            responseStatus = HttpStatus.ACCEPTED;
            responseMessage="You are not allowed to reset the password because link is not valid or broken";
        }

        responseJson.put("message",responseMessage);

        return new ResponseEntity(responseJson,responseStatus);
    }



}
