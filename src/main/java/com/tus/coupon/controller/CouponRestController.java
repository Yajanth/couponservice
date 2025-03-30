package com.tus.coupon.controller;

import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tus.coupon.model.Coupon;
import com.tus.coupon.repo.CouponRepo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/couponapi")
public class CouponRestController {
//comment for version control
	@Autowired
	CouponRepo repo;

	// this is called constructor based injection.
	// you can now use normal mocks to test
	/*
	 * public CouponRestController(CouponRepo repo) { this.repo=repo; }
	 */
	// setter injection
//	@Autowired
//	public void setRepo(CouponRepo repo) {
//		this.repo=repo;
//	}

	@PostMapping(value = "/coupons")
	public ResponseEntity<Coupon> create(@RequestBody Coupon coupon) {
		// return repo.save(coupon);
		return new ResponseEntity<>(repo.save(coupon), HttpStatus.OK);
	}

	@GetMapping("/coupons/{code}")
	Coupon getCouponByCouponCode(@PathVariable String code) {
		System.out.println(code);
		return repo.findByCode(code);
	}

	@GetMapping(value = "/coupons")
	public List<Coupon> getAllCoupons() {
		return repo.findAll();

	}

	@GetMapping("/stress-test")
	public String stressTest(@RequestParam(defaultValue = "10") int threads,
			@RequestParam(defaultValue = "10000") int iterations) {
		ExecutorService executor = Executors.newFixedThreadPool(threads);
		for (int i = 0; i < threads; i++) {
			executor.submit(() -> runMathProblem(iterations));
		}
		executor.shutdown();
		return "Stress test started with " + threads + " threads.";
	}

	private void runMathProblem(int iterations) {
		for (int i = 0; i < iterations; i++) {
			int num = 10000; // Example math problem
			Math.sqrt(num); // Simple computation to stress CPU
		}
	}
	
	// New endpoint to get instance name and IP address as a webpage
    @GetMapping("/instance-info")
    public String getInstanceInfo() {
        String instanceId = fetchMetadata("instance-id");
        String instanceIp = fetchMetadata("local-ipv4");
        
        // Return a basic HTML page with the instance information
        return "<html>" +
                "<head><title>Instance Info</title></head>" +
                "<body>" +
                "<h1>Instance Information</h1>" +
                "<p><strong>Instance ID:</strong> " + instanceId + "</p>" +
                "<p><strong>IP Address:</strong> " + instanceIp + "</p>" +
                "</body>" +
                "</html>";
    }
    // Helper method to fetch metadata from AWS EC2 instance
    private String fetchMetadata(String path) {
        try {
            // First, get a token
            URL tokenUrl = new URL("http://169.254.169.254/latest/api/token");
            HttpURLConnection tokenConn = (HttpURLConnection) tokenUrl.openConnection();
            tokenConn.setRequestMethod("PUT");
            tokenConn.setRequestProperty("X-aws-ec2-metadata-token-ttl-seconds", "21600");
            tokenConn.setDoOutput(true);
            
            BufferedReader tokenReader = new BufferedReader(new InputStreamReader(tokenConn.getInputStream()));
            String token = tokenReader.readLine();
            tokenReader.close();

            // Use the token to get the metadata
            URL url = new URL("http://169.254.169.254/latest/meta-data/" + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-aws-ec2-metadata-token", token);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String metadata = in.readLine();
            in.close();
            return metadata;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to fetch " + path;
        }
    }

}
