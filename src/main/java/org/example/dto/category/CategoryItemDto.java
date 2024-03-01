package org.example.dto.category;

import lombok.Data;
import org.example.entities.CategoryPhoto;

import java.util.List;

@Data
public class CategoryItemDto {
    private int id;
    private String name;
    private List<CategoryPhoto> images;
    private String description;
    private String dateCreated;
}
