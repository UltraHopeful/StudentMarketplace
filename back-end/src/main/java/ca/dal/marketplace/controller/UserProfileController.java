package ca.dal.marketplace.controller;

import ca.dal.marketplace.dao.BuyerRepository;
import ca.dal.marketplace.dao.UserRepository;
import ca.dal.marketplace.dto.UserDto;
import ca.dal.marketplace.entity.Buyer;
import ca.dal.marketplace.entity.User;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/user")
public class UserProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BuyerRepository buyerRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping(value = "/profile-details/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> displayUserProfile(@PathVariable Long id){
        // convert to user entity
//
        Optional<User> currentUser = userRepository.findById(id);
//        User existingUser = userRepository.findBy(user.getEmail()) ;

        String responseMessage = "";

        HttpStatus responseStatus = HttpStatus.ACCEPTED;

        Map<String,Object> responseJson = new HashMap<>();

        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        UserDto responseUserDto = modelMapper.map(currentUser, UserDto.class);
        responseUserDto.setId(currentUser.get().getId());
        responseUserDto.setEmail(currentUser.get().getEmail());
        responseUserDto.setFirstName(currentUser.get().getFirstName());
        responseUserDto.setLastName(currentUser.get().getLastName());
        responseUserDto.setPhoneNumber(currentUser.get().getPhoneNumber());
        responseUserDto.setDateOfBirth(currentUser.get().getDateOfBirth());


        responseStatus = HttpStatus.OK;
        
        responseJson.put("user",responseUserDto);

        responseJson.put("message",responseMessage);

        return new ResponseEntity(responseJson,responseStatus);
    }

    @PostMapping(value = "/edit-profile",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUserProfile(@RequestBody UserDto userDto){
        // convert to user entity

        User user = modelMapper.map(userDto,User.class);

        User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());

        String responseMessage = "";

        HttpStatus responseStatus = HttpStatus.ACCEPTED;

        Map<String,Object> responseJson = new HashMap<>();

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setDateOfBirth(userDto.getDateOfBirth());

        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        userRepository.save(existingUser);
        responseMessage = "updated successfully";
        UserDto.UserProfileResponseDto responseUserDto = modelMapper.map(existingUser, UserDto.UserProfileResponseDto.class);

        responseStatus = HttpStatus.OK;
        
        responseJson.put("user",responseUserDto);

        responseJson.put("message",responseMessage);

        return new ResponseEntity(responseJson,responseStatus);
    }

    @PostMapping(value = "/change-password",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changeUserPassword(@RequestBody Map<String, String> json){
        // convert to user entity

        String userEmail = json.get("userMail");

        String Old_password = json.get("Old_Password");
        String New_password = json.get("New_Password");


        User existingUser = userRepository.findByEmailIgnoreCase(userEmail);

        String responseMessage = "";

        HttpStatus responseStatus = HttpStatus.ACCEPTED;

        Map<String,Object> responseJson = new HashMap<>();

        if(encoder.matches(Old_password, existingUser.getPassword())) {
            existingUser.setPassword(encoder.encode(New_password));
            
            responseMessage = "user password updated successfully";
        }
        else {
            responseMessage = "user password not updated successfully";
        }
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        userRepository.save(existingUser);
        UserDto.UserProfileResponseDto responseUserDto = modelMapper.map(existingUser, UserDto.UserProfileResponseDto.class);

        responseStatus = HttpStatus.OK;

        responseJson.put("user",responseUserDto);

        responseJson.put("message",responseMessage);

        return new ResponseEntity(responseJson,responseStatus);
    }

    @PostMapping(value = "/delete-profile",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUserProfile(@RequestBody UserDto userDto){
        // convert to user entity

        User user = modelMapper.map(userDto,User.class);

        User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());
        System.out.println("UserID: " + existingUser.getId());
        System.out.println("UserEmail: " + existingUser.getEmail());
        System.out.println("UserFirstName: " + existingUser.getFirstName());

        String responseMessage = "";

        HttpStatus responseStatus = HttpStatus.ACCEPTED;

        Set<Buyer> buyers = buyerRepository.findByUser(existingUser);
        for (Buyer buyer: buyers) {
            buyerRepository.deleteById(buyer.getId());
        }

        Map<String,Object> responseJson = new HashMap<>();
        userRepository.deleteById(existingUser.getId());
        User checkUser = userRepository.findByEmailIgnoreCase(user.getEmail());
        if(checkUser==null) {
            responseMessage = "user deleted successfully";
        }else{
            responseMessage = "user not deleted successfully";
        }
        UserDto.UserProfileResponseDto responseUserDto = modelMapper.map(existingUser, UserDto.UserProfileResponseDto.class);

        responseStatus = HttpStatus.OK;

        responseJson.put("user",responseUserDto);

        responseJson.put("message",responseMessage);

        return new ResponseEntity(responseJson,responseStatus);
    }
}
