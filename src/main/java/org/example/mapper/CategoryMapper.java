package org.example.mapper;

import org.example.dto.category.CategoryEditDto;
import org.example.dto.category.CategoryItemDto;
import org.example.entities.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source="dateTime",target="dateCreated",dateFormat ="dd.MM.yyy HH:mm:ss")
    CategoryItemDto categoryItemDto(CategoryEntity categoryEntity);

    @Mapping(target = "images", ignore = true)
    List<CategoryItemDto> categoryListItemSto(List<CategoryEntity> listCategoryEntity);

    @Mapping(target = "images", ignore = true)
    CategoryEntity categoryEditDto(CategoryEditDto dto);
}
