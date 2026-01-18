package com.practice.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 3, message = "Product name must contain atleast 3 character")
    private String productName;
    @NotBlank
    @Size(min = 3, message = "Product description must contain least 6 character")
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
