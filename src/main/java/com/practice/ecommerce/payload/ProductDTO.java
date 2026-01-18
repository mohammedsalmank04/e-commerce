package com.practice.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String productName;
    private String description;
    private String image;
    private Integer quantity;
    private Double price  ;
    private Double specialPrice ;
    private Double discount ;
}
