package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.dto.category.CategoryCreateDto;
import org.example.dto.category.CategoryEditDto;
import org.example.entities.CategoryEntity;
import org.example.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {

    private final String filePath="D:\\SHAG\\Java\\HW\\HW(Java)_3\\react_java_3\\public\\photos\\";
    private final CategoryRepository categoryRepository;
    private final ModelMapper _mapper = new ModelMapper();

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<List<CategoryEntity>> index(){
        List<CategoryEntity> list = categoryRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryEntity> create(@ModelAttribute CategoryCreateDto dto) throws IOException {

        URL url = new URL(dto.getImage());
        BufferedImage _image = ImageIO.read(url);

        String imageName = UUID.randomUUID().toString()+".jpg";
        Path fullPath = Paths.get(filePath+imageName);
        ImageIO.write(_image,"jpg",fullPath.toFile());
        dto.setImage(imageName);

        CategoryEntity _category = _mapper.map(dto,CategoryEntity.class);
        _category.setDateTime(LocalDateTime.now());
        categoryRepository.save(_category);

        return new ResponseEntity(_category,HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping(value = "edit",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryEntity>edit(@ModelAttribute CategoryEditDto dto) throws IOException {
        Optional<CategoryEntity> _editCateg = categoryRepository.findById(dto.getId());
        if(_editCateg.isEmpty())return  new ResponseEntity("Продукт не знайдено",HttpStatus.NOT_FOUND);

        CategoryEntity categ = _editCateg.get();
        if(!dto.getName().isEmpty())categ.setName(dto.getName());
        if(!dto.getImage().isEmpty()){
            if(Files.exists(Path.of(filePath + categ.getImage()))){
                Files.delete(Path.of(filePath + categ.getImage()));
            }
        URL url = new URL(dto.getImage());
        BufferedImage _image = ImageIO.read(url);

        String imageName = UUID.randomUUID().toString()+".jpg";
        Path fullPath = Paths.get(filePath+imageName);
        ImageIO.write(_image,"jpg",fullPath.toFile());
        dto.setImage(imageName);

        }
        if(!dto.getDescription().isEmpty())categ.setDescription(dto.getDescription());




        //CategoryEntity _category = _mapper.map(dto,CategoryEntity.class);
        categ.setDateTime(LocalDateTime.now());
        categoryRepository.save(categ);
        return new ResponseEntity(categ,HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) throws IOException {
        Optional<CategoryEntity> _deleteCateg = categoryRepository.findById(id);
        if(_deleteCateg.isEmpty())return  new ResponseEntity("Продукт не знайдено",HttpStatus.NOT_FOUND);

        CategoryEntity categ = _deleteCateg.get();
        if(Files.exists(Path.of(filePath + categ.getImage()))){
            Files.delete(Path.of(filePath + categ.getImage()));
        }

        CategoryEntity _category = _deleteCateg.get();
        categoryRepository.delete(_category);

        return  new ResponseEntity("Продукт успішно видалено",HttpStatus.OK);
    }

}
