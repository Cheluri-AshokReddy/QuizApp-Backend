package com.quizserver.quizserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    private long id;
    private String fullname;
    private String mobileNumber;
    private String email;
    private String address;
    private String highestQualification;
    private String userProfileRole;
    private String gender;
    private Long userId;
}
