package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "tbl_categ")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255,nullable = false)
    private String name;

    @Column(length = 255,nullable = false)
    private String image;

    @Column(length = 3500,nullable = false)
    private String Description;

    @Column(name = "date_created")
    //@Temporal(TemporalType.TIMESTAMP)
    //private Date dateCreated;
    private LocalDateTime dateTime;

}
