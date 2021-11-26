package com.andreasmarsh.SpringTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class PromotionRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PromotionRepository repo;

    // test methods go below
    @Test
    public void testCreatePromotion() {
        Promotion promo = new Promotion();
        promo.setPercentage(50L);
        promo.setCode("Save50");

        Date date = new Date();
        promo.setStartTime(date);
        promo.setStartDate(date);

        Date date2 = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date2);
        c.add(Calendar.DATE, 1);
        date2 = c.getTime();

        promo.setEndTime(date2);
        promo.setEndDate(date2);

        Promotion savedPromo = repo.save(promo);

        Promotion existPromo = entityManager.find(Promotion.class, savedPromo.getPromotionID());

        assertThat(promo.getPromotionID()).isEqualTo(existPromo.getPromotionID());
    }
}
