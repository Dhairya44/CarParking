package com.andreasmarsh.SpringTest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Query("SELECT u FROM Promotion u WHERE u.promotionID = ?1")
    public Optional<Promotion> findById(Long id);

    @Query("SELECT u FROM Promotion u WHERE u.code = ?1")
    public Promotion findByCode(String code);

}
