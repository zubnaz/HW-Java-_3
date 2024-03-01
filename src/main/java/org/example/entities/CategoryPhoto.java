package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="tbl_photos")
public class CategoryPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name",length = 255,nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_entity_id", nullable = false)
    @JsonIgnore
    private CategoryEntity category;

}
