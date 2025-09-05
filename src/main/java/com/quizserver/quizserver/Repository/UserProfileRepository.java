package com.quizserver.quizserver.Repository;

import com.quizserver.quizserver.Model.User;
import com.quizserver.quizserver.Model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByFullname(String name);

    Optional<Object> findByUser(User user);

    Optional<UserProfile> findByUserId(Long userId);
}
