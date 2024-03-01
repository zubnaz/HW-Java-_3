package org.example;

import org.example.entities.CategoryEntity;
import org.example.entities.CategoryPhoto;
import org.example.repositories.CategoryRepository;
import org.example.storage.StorageProperties;
import org.example.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
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
    CommandLineRunner runner(CategoryRepository repository, StorageService service) {
        return args -> {
            try {
                service.init();
                CategoryEntity category = new CategoryEntity();
                category.setName("Продукти");
                category.setDescription("Корисні");
                CategoryPhoto photo = new CategoryPhoto();
                photo.setName("product.jpg");
                photo.setCategory(category);
                List<CategoryPhoto> listPhotos = new ArrayList<>();
                listPhotos.add(photo);
                category.setImages(listPhotos);
                category.setDateTime(LocalDateTime.now());

                //repository.save(category);
            }catch (Exception ex){
                System.out.println("Помилка ініціалізації додатку : "+ ex.getMessage());
            }
        };
    }
}