package com.sid.authservice;

import com.sid.authservice.sec.entites.AppRole;
import com.sid.authservice.sec.entites.AppUser;
import com.sid.authservice.sec.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }


    @Bean
    CommandLineRunner start(AccountService accountService) {
        return args -> {
            accountService.addNewRole(new AppRole(null, "USER"));
            accountService.addNewRole(new AppRole(null, "ADMIN"));
            accountService.addNewRole(new AppRole(null, "CUSTOMER_ADMIN"));
            accountService.addNewRole(new AppRole(null, "PRODUCT_ADMIN"));
            accountService.addNewRole(new AppRole(null, "BILLS_ADMIN"));

            accountService.addNewUser(new AppUser(null, "user1", "1234", true, new ArrayList<>()));
            accountService.addNewUser(new AppUser(null, "admin", "1234", true, new ArrayList<>()));
            accountService.addNewUser(new AppUser(null, "user2", "1234", true, new ArrayList<>()));
            accountService.addNewUser(new AppUser(null, "user3", "1234", true, new ArrayList<>()));
            accountService.addNewUser(new AppUser(null, "user4", "1234", true, new ArrayList<>()));

            accountService.addRoleToUser("user1", "USER");
            accountService.addRoleToUser("admin", "USER");
            accountService.addRoleToUser("admin", "ADMIN");
            accountService.addRoleToUser("user2", "CUSTOMER_ADMIN");
            accountService.addRoleToUser("user3", "PRODUCT_ADMIN");
            accountService.addRoleToUser("user4", "BILLS_ADMIN");


        };
    }
}
