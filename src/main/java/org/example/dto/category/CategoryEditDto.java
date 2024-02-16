package org.example.dto.category;

import lombok.Data;

@Data
public class CategoryEditDto {
    private int id;
    private String name;
    private String image;
    private String description;
}
