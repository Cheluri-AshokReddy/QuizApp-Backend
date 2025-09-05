package com.quizserver.quizserver.Service;

import com.quizserver.quizserver.DTO.UserProfileDTO;
import com.quizserver.quizserver.Model.User;
import com.quizserver.quizserver.Model.UserProfile;

public interface UserService {
    User createUser(User user);
    Boolean hasUserWithEmail(String email);
    User login(User user);
    UserProfileDTO getUserProfile(Long id , UserProfile userProfile);
    UserProfileDTO findUserProfile(Long userid);

}
