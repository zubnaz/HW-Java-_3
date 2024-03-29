package org.example.dto.category;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CategoryEditDto {
    private int id;
    private String name;
    private List<MultipartFile> images;
    private String description;
}
