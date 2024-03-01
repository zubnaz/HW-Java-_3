package org.example.controllers;

import lombok.AllArgsConstructor;
import org.example.dto.category.CategoryCreateDto;
import org.example.dto.category.CategoryEditDto;
import org.example.dto.category.CategoryItemDto;
import org.example.entities.CategoryEntity;
import org.example.entities.CategoryPhoto;
import org.example.mapper.CategoryMapper;
import org.example.repositories.CategoryRepository;
import org.example.repositories.PhotoRepository;
import org.example.storage.FileSaveFormat;
import org.example.storage.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.*;
import java.util.List;

import static java.util.Comparator.comparing;

@RestController
@AllArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {

    private final String filePath="D:\\SHAG\\Java\\HW\\HW(Java)_3\\react_java_3\\public\\photos\\";
    private final CategoryRepository categoryRepository;
    private final PhotoRepository photoRepository;
    private final StorageService storageService;
    private final ModelMapper _mapper = new ModelMapper();

    private CategoryMapper categoryMapper;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<List<CategoryItemDto>> index(){
        List<CategoryItemDto> list = categoryMapper.categoryListItemSto(categoryRepository.findAll());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryEntity> create(@ModelAttribute CategoryCreateDto dto) throws IOException {
        try{
            CategoryEntity _category = _mapper.map(dto,CategoryEntity.class);
            List<CategoryPhoto> photos = new ArrayList<>();
            for (var item :dto.getImage()){
                String imageName = storageService.SaveImage(item, FileSaveFormat.JPG);
                CategoryPhoto ctgPht = new CategoryPhoto();
                ctgPht.setName(imageName);
                ctgPht.setCategory(_category);
                photos.add(ctgPht);
            }
            _category.setImages(photos);
            _category.setDateTime(LocalDateTime.now());
            categoryRepository.save(_category);

            return new ResponseEntity(_category,HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping(value = "edit",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryEntity>edit(@ModelAttribute CategoryEditDto dto) throws IOException {
        Optional<CategoryEntity> _editCateg = categoryRepository.findById(dto.getId());
        if(_editCateg.isEmpty())return  new ResponseEntity("Продукт не знайдено",HttpStatus.NOT_FOUND);

        CategoryEntity categ = _editCateg.get();
        if(!dto.getName().isEmpty())categ.setName(dto.getName());
        if(!(dto.getImages()==null)&&!dto.getImages().isEmpty()) {
            for(var item:categ.getImages()){
                storageService.DeleteImages(item.getName());
                photoRepository.deleteById(item.getId());
            }
            List<CategoryPhoto> photos = new ArrayList<>();
            for (var item :dto.getImages()){
                String imageName = storageService.SaveImage(item, FileSaveFormat.JPG);
                CategoryPhoto ctgPht = new CategoryPhoto();
                ctgPht.setName(imageName);
                ctgPht.setCategory(categ);
                photos.add(ctgPht);
            }
            categ.setImages(photos);
        }
        if(!dto.getDescription().isEmpty())categ.setDescription(dto.getDescription());

        categ.setDateTime(LocalDateTime.now());
        categoryRepository.save(categ);
        return new ResponseEntity(categ,HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) throws IOException {
        Optional<CategoryEntity> _deleteCateg = categoryRepository.findById(id);
        if(_deleteCateg.isEmpty())return  new ResponseEntity("Продукт не знайдено",HttpStatus.NOT_FOUND);
        
        CategoryEntity _category = _deleteCateg.get();
        for (var item:_category.getImages()){
            storageService.DeleteImages(item.getName());
            photoRepository.deleteById(item.getId());
        }

        categoryRepository.delete(_category);

        return  new ResponseEntity("Продукт успішно видалено",HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "find_by_id/{id}")
    public ResponseEntity<CategoryEntity> get_by_id(@PathVariable int id) throws IOException {
        Optional<CategoryEntity> _Categ = categoryRepository.findById(id);
        if(_Categ.isEmpty())return  new ResponseEntity("Продукт не знайдено",HttpStatus.NOT_FOUND);

        return  new ResponseEntity(_Categ,HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<CategoryItemDto>> searchByName(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false)String description,
                                                              Pageable pageable) {
        System.out.println("Name - "+name+" Desc - "+description);

        Page<CategoryEntity> categoriesName = categoryRepository.findByNameContainingIgnoreCase(name, pageable);
        System.out.println("Name : ");
        for (var it :categoriesName){
            System.out.println(it.getId());
        }

        Page<CategoryEntity> categoriesDescription = categoryRepository.findByDescriptionContainingIgnoreCase(description,pageable);
        System.out.println("Desc : ");
        for (var it :categoriesDescription){
            System.out.println(it.getId());
        }

       List<CategoryEntity> categoriesList = new ArrayList<>();
       for (var item : categoriesName){
           for (var item2 : categoriesDescription){
               if(item.getId()==item2.getId())categoriesList.add(item);

           }
       }
        Page<CategoryEntity> categories = new PageImpl<>(categoriesList,pageable,categoriesList.size());

        Page<CategoryItemDto> result = categories.map(categoryMapper::categoryItemDto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/sort_name")
    public ResponseEntity<Page<CategoryItemDto>> sortByName(@RequestParam(required = false) String name,
                                                            @RequestParam(required = false)String description,
                                                            Pageable pageable) {
        Page<CategoryEntity> categoriesName = categoryRepository.findByNameContainingIgnoreCase(name, pageable);
        Page<CategoryEntity> categoriesDescription = categoryRepository.findByDescriptionContainingIgnoreCase(description,pageable);
        List<CategoryEntity> categoriesList = new ArrayList<>();
        for (var item : categoriesName){
            for (var item2 : categoriesDescription){
                if(item.getId()==item2.getId())categoriesList.add(item);

            }
        }

        categoriesList.sort((a,b)->a.getName().compareToIgnoreCase(b.getName()));
        Page<CategoryEntity> categories = new PageImpl<>(categoriesList,pageable,categoriesList.size());

        Page<CategoryItemDto> sortCategories = categories.map(categoryMapper::categoryItemDto);

        return new ResponseEntity<>(sortCategories, HttpStatus.OK);
    }
    @GetMapping("/sort_description")
    public ResponseEntity<Page<CategoryItemDto>> sortByDescription(@RequestParam(required = false) String name,
                                                                   @RequestParam(required = false)String description,
                                                                   Pageable pageable) {
        Page<CategoryEntity> categoriesName = categoryRepository.findByNameContainingIgnoreCase(name, pageable);
        Page<CategoryEntity> categoriesDescription = categoryRepository.findByDescriptionContainingIgnoreCase(description,pageable);
        List<CategoryEntity> categoriesList = new ArrayList<>();
        for (var item : categoriesName){
            for (var item2 : categoriesDescription){
                if(item.getId()==item2.getId())categoriesList.add(item);

            }
        }

        categoriesList.sort((a,b)->a.getDescription().compareToIgnoreCase(b.getDescription()));
        Page<CategoryEntity> categories = new PageImpl<>(categoriesList,pageable,categoriesList.size());

        Page<CategoryItemDto> sortCategories = categories.map(categoryMapper::categoryItemDto);

        return new ResponseEntity<>(sortCategories, HttpStatus.OK);
    }
}
