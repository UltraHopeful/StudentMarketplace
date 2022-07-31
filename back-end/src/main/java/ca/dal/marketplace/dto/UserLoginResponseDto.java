package ca.dal.marketplace.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserLoginResponseDto {
    private Long id;
    private String firstName;
    private String email;
}
