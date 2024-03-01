package org.example.repositories;

import org.example.entities.CategoryEntity;
import org.example.entities.CategoryPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<CategoryPhoto,Integer> {

}
