package ca.dal.marketplace.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Data
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Date dateOfBirth;
    private Integer failedAttempts;
    private boolean isLocked;
    private boolean isEnabled;
    private Date insertTimestamp;

    @Data
    @Getter
    @Setter
    public static class UserLoginResponse {
        private Long id;
        private String firstName;
        private String email;
    }

    @Data
    @Getter
    @Setter
    public static class UserProfileResponseDto {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private Date dateOfBirth;
    }
}
