package com.temitope.myblogapp.repository;

import com.temitope.myblogapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

//    Optional<User> findByUsernameOrEmail(String userDetails);
    Optional<User> findByUsername(String userDetails);
    boolean existsByEmail(String email);
    boolean existsByUsername(String userName);
}
