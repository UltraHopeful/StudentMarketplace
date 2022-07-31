package ca.dal.marketplace.controller;

import ca.dal.marketplace.dao.UserRepository;
import ca.dal.marketplace.dto.UserDto;
import ca.dal.marketplace.dto.UserLoginResponseDto;
import ca.dal.marketplace.entity.User;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/user")
public class UserLoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Environment environment;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {

        User user = modelMapper.map(userDto,User.class);

        int maxLoginAttempt = Integer.parseInt(environment.getProperty("spring.user.maxLoginAttempt"));

        User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());

        String responseMessage = "";

        HttpStatus responseStatus = HttpStatus.ACCEPTED;

        Map<String,Object> responseJson = new HashMap<>();

        if(existingUser != null) {
            int remainingAttempts;
            int failedAttempt = existingUser.getFailedAttempts();
            if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword()) && existingUser.isEnabled()) {
                responseStatus = HttpStatus.OK;
                responseMessage = "Login Successfully";
                existingUser.setFailedAttempts(0);
                userRepository.save(existingUser);
//                responseJson.put("username",existingUser.getFirstName());
//                responseJson.put("userEmail",existingUser.getEmail());
//                responseJson.put("userId",existingUser.getId());

                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                UserDto responseUserDto = modelMapper.map(existingUser, UserDto.class);

                responseJson.put("user",responseUserDto);
            }
            else if(existingUser.isEnabled() == false){
                responseStatus = HttpStatus.ACCEPTED;
                responseMessage = "You have to first verify your account";
            }
            else {
                failedAttempt += 1;
                if(failedAttempt >= maxLoginAttempt){
                    existingUser.setFailedAttempts(maxLoginAttempt);
                    existingUser.setLocked(true);
                    userRepository.save(existingUser);
                    responseStatus = HttpStatus.ACCEPTED;
                    responseMessage = "Sorry your account is locked, please reset your password";
                }else {
                    existingUser.setFailedAttempts(failedAttempt);
                    remainingAttempts = maxLoginAttempt-failedAttempt;
                    // update user after failedAttempt
                    userRepository.save(existingUser);
                    responseStatus = HttpStatus.OK;
                    responseMessage = "Sorry you entered wrong password, you still have:" + remainingAttempts + " left";
                }
            }
        }
        else {
            responseStatus = HttpStatus.ACCEPTED;
            responseMessage = "Sorry no account connected with this email so you have to create new account";
        }

        responseJson.put("message",responseMessage);

        return new ResponseEntity(responseJson,responseStatus);
    }
}