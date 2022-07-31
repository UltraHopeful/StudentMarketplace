package ca.dal.marketplace.controller;

import ca.dal.marketplace.dao.UniversityDomainRepository;
import ca.dal.marketplace.dao.UserRepository;
import ca.dal.marketplace.dao.UserSecurityTokenRepository;
import ca.dal.marketplace.dto.UniversityDomainDto;
import ca.dal.marketplace.dto.UserDto;
import ca.dal.marketplace.dto.UserLoginResponseDto;
import ca.dal.marketplace.entity.User;
import ca.dal.marketplace.entity.UserSecurityToken;
import ca.dal.marketplace.service.MailService;
import ca.dal.marketplace.service.UserRegistrationService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/user")
public class UserRegistrationController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityDomainRepository universityDomainRepository;

    @Autowired
    private UserSecurityTokenRepository userSecurityTokenRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private ModelMapper modelMapper;


    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    // whenever user enters details with form it will direct to this api
    @PostMapping(value = "/register",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user, BindingResult validationResult) {
        String responseMessage = "";

        HttpStatus responseStatus = HttpStatus.ACCEPTED;

        Map<String,Object> responseJson = new HashMap<>();

        String homeUrl = environment.getProperty("spring.url.home");
        String userVerifyUrl = environment.getProperty("spring.url.verifyUser");

        String emailSubject = "Student Market Place | Complete Registration! But now you have to verify your account";
        String emailMessage = "To verify your account please click link below :\n";

        if(validationResult.hasErrors()) {
            responseStatus = HttpStatus.NOT_ACCEPTABLE;
            responseMessage = "Error in field "+validationResult.getFieldError();
        }
        else{
            User userExists = userRepository.findByEmailIgnoreCase(user.getEmail());
            String emailField = user.getEmail();
            String emailDomain = emailField.substring(emailField.indexOf("@")+1);
            UniversityDomainDto foundDomains;
            foundDomains = universityDomainRepository.findByUniversityDomain(emailDomain);
            // if user object is not null then we already have that email in system then don't add to the database
            if (foundDomains == null) {
                responseStatus = HttpStatus.FORBIDDEN;
                responseMessage = "You have to enter your school or university Email";
            }
            // if user object is not null then we already have that email in system then don't add to the database
            else if (userExists != null) {
                responseStatus = HttpStatus.OK;
                responseMessage = "Please try with another email this email is used.";
                UserLoginResponseDto responseUserDto = modelMapper.map(userExists, UserLoginResponseDto.class);
                responseJson.put("user",responseUserDto);
            }
            // user is not exists in database
            else {
                // getting email of user for user object
                String userEmail = user.getEmail();
                // getting account verification object with token
                UserSecurityToken userSecurityToken = new UserSecurityToken(user);

                emailMessage+=homeUrl+""+userVerifyUrl+"?token=" + userSecurityToken.getToken();

                // checking the mail is send or not
                boolean isMailSend = mailService.sendTokenInMail(userEmail, emailSubject, emailMessage);

                // if mail is send then we add user and account verification details in the database
                if (isMailSend) {
                    userRegistrationService.registerUser(user);
                    this.userSecurityTokenRepository.save(userSecurityToken);
                    responseStatus = HttpStatus.OK;
                    responseMessage = "Please check your mail " + userEmail + " for verification link";
                } else {
                    responseStatus = HttpStatus.ACCEPTED;
                    responseMessage="Some error sending mail. So can you try some time after";
                }
            }
        }

        responseJson.put("message",responseMessage);

        return new ResponseEntity(responseJson,responseStatus);
    }

    // after receiving the verification link in mail user click on it and redirect to site
    // so check the verification token in the database from link
    @RequestMapping(value = "/verify", method = {RequestMethod.GET, RequestMethod.POST},produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userConfirmation(@RequestParam("token")String authToken){
        UserSecurityToken receivedVerificationToken = userSecurityTokenRepository.findByToken(authToken);

        String responseMessage = "";

        HttpStatus responseStatus = HttpStatus.ACCEPTED;

        Map<String,String> responseJson = new HashMap<>();

        // if we verified the token from link then set isEnabled true for that user
        if(receivedVerificationToken != null){
            User user = userRepository.findByEmailIgnoreCase(receivedVerificationToken.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
            responseStatus = HttpStatus.OK;
            responseMessage = "You verified successfully!";
        }
        // if the link is not verified so it is broken or invalid link
        else{
            responseStatus = HttpStatus.ACCEPTED;
            responseMessage="You are not verified because link is not valid or broken";
        }

        responseJson.put("message",responseMessage);

        return new ResponseEntity(responseJson,responseStatus);
    }
}
