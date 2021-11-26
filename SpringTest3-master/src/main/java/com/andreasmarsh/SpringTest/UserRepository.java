package com.andreasmarsh.SpringTest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String code);

    @Query("SELECT u FROM User u WHERE u.userID = ?1")
    public Optional<User> findById(Long id);

    @Query("SELECT u FROM User u WHERE u.promotions = ?1")
    public List<User> findByPromotions(Boolean promotions);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ?1)", nativeQuery=true)
    public BigInteger doesEmailExist(String email);
}