package org.example.dto.category;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CategoryCreateDto {
    private String name;
    private List<MultipartFile> image;
    private String description;
}
