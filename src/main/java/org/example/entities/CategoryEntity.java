package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "tbl_categ")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255,nullable = false)
    private String name;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<CategoryPhoto> images;

    @Column(length = 3500,nullable = false)
    private String description;

    @Column(name = "date_created")
    //@Temporal(TemporalType.TIMESTAMP)
    //private Date dateCreated;
    private LocalDateTime dateTime;

}
