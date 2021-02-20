package com.sid.billingservice;


import com.sid.billingservice.entities.Bill;
import com.sid.billingservice.entities.ProductItem;
import com.sid.billingservice.feign.CustomerServiceClient;
import com.sid.billingservice.feign.InventoryServiceClient;
import com.sid.billingservice.model.Customer;
import com.sid.billingservice.repository.BillRepository;
import com.sid.billingservice.repository.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import java.util.Date;

@EnableFeignClients
@SpringBootApplication
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner start(BillRepository billRepository, ProductItemRepository productItemRepository, InventoryServiceClient inventoryServiceClient, CustomerServiceClient customerServiceClient, RepositoryRestConfiguration restConfiguration) {
        restConfiguration.exposeIdsFor(Bill.class, ProductItem.class);
        return args -> {
            Bill bill = new Bill();
            bill.setBillingDate(new Date());
            Customer customer = customerServiceClient.findCustomerById(1L);
            bill.setCustomerID(customer.getId());
            billRepository.save(bill);
            inventoryServiceClient.findAll().getContent().forEach(p -> productItemRepository.save(new ProductItem(null, null, p.getId(), p.getPrice(), (int) (1 + Math.random() * 1000), bill)));
        };
    }
}
