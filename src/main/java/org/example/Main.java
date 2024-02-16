package org.example;

import org.example.entities.CategoryEntity;
import org.example.repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");
        SpringApplication.run(Main.class,args);
        
    }
    @Bean
    CommandLineRunner runner(CategoryRepository repository) {
        return args -> {
            CategoryEntity category = new CategoryEntity();
            category.setName("Продукти");
            category.setDescription("Корисні");
            category.setImage("products.jpg");
            category.setDateTime(LocalDateTime.now());

            //repository.save(category);
        };
    }
}