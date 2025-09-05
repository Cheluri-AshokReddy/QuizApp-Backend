package com.quizserver.quizserver.Service;

import com.quizserver.quizserver.DTO.UserProfileDTO;
import com.quizserver.quizserver.Model.User;
import com.quizserver.quizserver.Model.UserProfile;
import com.quizserver.quizserver.Repository.UserProfileRepository;
import com.quizserver.quizserver.Repository.UserRepository;
import com.quizserver.quizserver.emuns.UserRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final ModelMapper modelMapper;


    @PostConstruct
    private void createAdminUser() {
        User optionalUser = userRepository.findByRole(UserRole.ADMIN);
        if (optionalUser == null) {
            User user = new User();

            user.setName("Admin");
            user.setEmail("admin@gmail.com");
            user.setRole(UserRole.ADMIN);
            user.setPassword("admin");

            userRepository.save(user);
        }
    }

    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }


    @Override
    public User createUser(User user) {
        user.setRole(UserRole.USER);
        User savedUser = userRepository.save(user);
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(savedUser);
        userProfile.setFullname(user.getName());
        userProfileRepository.save(userProfile);

        return savedUser;
    }

    public User login(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        if (optionalUser.isPresent() && user.getPassword().equals(optionalUser.get().getPassword())) {
            return optionalUser.get();
        }

        return null;
    }

    @Override
    public UserProfileDTO getUserProfile(Long id, UserProfile userProfile) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        // ✅ fetch existing profile
        UserProfile existingProfile = (UserProfile) userProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + id));

        // ✅ update only allowed fields
        existingProfile.setFullname(userProfile.getFullname());
        existingProfile.setMobileNumber(userProfile.getMobileNumber());
        existingProfile.setEmail(userProfile.getEmail());
        existingProfile.setAddress(userProfile.getAddress());
        existingProfile.setHighestQualification(userProfile.getHighestQualification());
        existingProfile.setUserProfileRole(userProfile.getUserProfileRole());
        existingProfile.setGender(userProfile.getGender());

        // user is already linked, no need to set again
        UserProfile updatedProfile = userProfileRepository.save(existingProfile);

        return modelMapper.map(updatedProfile, UserProfileDTO.class);
    }

    @Override
    public UserProfileDTO findUserProfile(Long userid) {
        Optional<UserProfile> profile = userProfileRepository.findByUserId(userid);
        if (profile.isPresent()) {
            return modelMapper.map(profile.get(), UserProfileDTO.class);
        }
        return null;
    }



}
