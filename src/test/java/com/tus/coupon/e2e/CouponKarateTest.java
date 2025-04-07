package com.tus.coupon.e2e;

import com.intuit.karate.junit5.Karate;

public class CouponKarateTest {
    @Karate.Test
    Karate testCouponAPI() {
        return Karate.run("create-coupon").relativeTo(getClass());
    }
}
