package com.tus.coupon.integration;

import com.tus.coupon.model.Coupon;
import com.tus.coupon.repo.CouponRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CouponRepo couponRepo;

    @Test
    void createAndFetchCouponIntegrationTest() {
        Coupon coupon = new Coupon(null, "INTEGRATE1", new BigDecimal("15.00"), "2025-12-31");
        ResponseEntity<Coupon> postResponse = restTemplate.postForEntity("/couponapi/coupons", coupon, Coupon.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Coupon created = postResponse.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getCode()).isEqualTo("INTEGRATE1");

        Coupon fetched = restTemplate.getForObject("/couponapi/coupons/INTEGRATE1", Coupon.class);
        assertThat(fetched.getDiscount()).isEqualTo(new BigDecimal("15.00"));
    }
    
}
