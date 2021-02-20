package com.sid.customerservices;

import com.sid.customerservices.entities.Customer;
import com.sid.customerservices.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class CustomerServicesApplication {

    public static void main(String[] args) {

        SpringApplication.run(CustomerServicesApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository, RepositoryRestConfiguration restConfiguration) {
        restConfiguration.exposeIdsFor(Customer.class);
        return args -> {
            customerRepository.save(new Customer(null, "Asmae", "asmae@gmail.ma"));
            customerRepository.save(new Customer(null, "Hiba", "hiba@gmail.ma"));
            customerRepository.save(new Customer(null, "elkabira", "elkabira@gmail.ma"));
            customerRepository.findAll().forEach(System.out::println);

        };
    }
}