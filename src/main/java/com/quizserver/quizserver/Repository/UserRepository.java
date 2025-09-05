package com.quizserver.quizserver.Repository;

import com.quizserver.quizserver.Model.User;
import com.quizserver.quizserver.emuns.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByRole(UserRole userRole);

    Optional<User> findFirstByEmail(String email);

    Optional<User> findByEmail(String email);
}
