package com.quizserver.quizserver.Controller;

import com.quizserver.quizserver.DTO.UserProfileDTO;
import com.quizserver.quizserver.Model.User;
import com.quizserver.quizserver.Model.UserProfile;
import com.quizserver.quizserver.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody User user) {
        if (userService.hasUserWithEmail(user.getEmail())) {
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        User createdUser = userService.createUser(user);
        if (createdUser == null) {
            return new ResponseEntity<>("User not created, come again later", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User dbUser = userService.login(user);

        if (dbUser == null) {
            return new ResponseEntity<>("Wrong Conditionals", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(dbUser, HttpStatus.OK);
    }

    @PutMapping("/userprofile/{userid}")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfile userProfile, @PathVariable Long userid) {
        UserProfileDTO userProfile1=userService.getUserProfile(userid, userProfile);

        if (userProfile1 == null) {
            return new ResponseEntity<>("UserProfile Already Created", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(userProfile1, HttpStatus.OK);
    }


    @GetMapping("/userprofile/{userid}")
    public ResponseEntity<?> getProfile(@PathVariable Long userid) {
        UserProfileDTO userProfile = userService.findUserProfile(userid);
        if (userProfile == null) {
            return new ResponseEntity<>("Profile not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }



}
