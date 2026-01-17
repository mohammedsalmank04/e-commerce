package com.practice.ecommerce.service;

import com.practice.ecommerce.Repositories.CategoryRepository;
import com.practice.ecommerce.Repositories.ProductRepository;
import com.practice.ecommerce.exceptions.ResourceNotFoundException;
import com.practice.ecommerce.model.Category;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.payload.ProductDTO;
import com.practice.ecommerce.payload.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private  CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;



    @Override
    public ProductDTO addProduct(Product product, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("category","categoryId",categoryId));

        double specicalPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());

        product.setCategory(category);
        product.setSpecialPrice(specicalPrice);
        product.setImage("default.png");

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> productList = productRepository.findAll();

       List<ProductDTO> productDTOSList = productList.stream().map(product ->
            modelMapper.map(product,ProductDTO.class)).toList();
       ProductResponse productResponse = new ProductResponse();
       productResponse.setProductsList(productDTOSList);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("category","categoryId",categoryId));

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);

        List<ProductDTO> productDTOList = products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductsList(productDTOList);
        return productResponse;
    }
}
