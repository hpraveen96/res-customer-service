package com.egov.customerservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
@RequestMapping("api/v1")
public class MainRestController {
    private static final Logger logger = LoggerFactory.getLogger(MainRestController.class);

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TokenService tokenService;

    @PostMapping("save/customer")
    public ResponseEntity<String> addCustomer(@RequestHeader("Authorization") String token,
                                             @RequestBody Customer customer) {
        logger.info("Received parameter for customer"+ customer.toString());
        String phone;
        try {
            phone = tokenService.validateToken(token);
        }catch(WebClientResponseException e){
            logger.info("Token validation failed: " + e.getMessage());
            return ResponseEntity.status(401).body("Invalid token");
        }
        if(phone.equals(customer.getPhone())) {
            logger.info("Phone Match, Saving Customer details");
            customerRepository.save(customer);
            return ResponseEntity.ok("Customer added Successfully");
        }

        return ResponseEntity.status(401).body("Invalid Phone Number");



    }
}
