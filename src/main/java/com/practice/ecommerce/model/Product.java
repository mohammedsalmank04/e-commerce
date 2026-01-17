package com.practice.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String description;
    private String image;
    private Integer quantity;
    private Double price  ;
    private Double specialPrice ;
    private Double discount ;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
