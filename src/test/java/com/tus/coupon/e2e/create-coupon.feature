Feature: Coupon API

  Scenario: Create and fetch a coupon
    Given url 'http://localhost:9095/couponapi/coupons'
    And request { code: 'KARATE10', discount: 15.00, expDate: '2025-12-31' }
    When method post
    Then status 200

    Given url 'http://localhost:9095/couponapi/coupons/KARATE10'
    When method get
    Then status 200
    And match response.code == 'KARATE10'
