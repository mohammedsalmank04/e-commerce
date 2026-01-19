package com.practice.ecommerce.controller;

import com.practice.ecommerce.config.AppConstants;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.payload.ProductDTO;
import com.practice.ecommerce.payload.ProductResponse;
import com.practice.ecommerce.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private  ProductService productService;



    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO product, @PathVariable Long categoryId){

        ProductDTO productDto = productService.addProduct(product, categoryId);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(defaultValue = AppConstants.pageNumber , required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.pageSize, required = false) Integer pageSize,
            @RequestParam(defaultValue = "id" , required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String orderBy
    ){
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, orderBy);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = AppConstants.pageNumber , required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.pageSize, required = false) Integer pageSize,
            @RequestParam(defaultValue = "id" , required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String orderBy
            ){
        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy,orderBy);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(
            @PathVariable String keyword,
            @RequestParam(defaultValue = AppConstants.pageNumber , required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.pageSize, required = false) Integer pageSize,
            @RequestParam(defaultValue = "id" , required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String orderBy
    ){
        ProductResponse productResponse = productService.searchProductByKeyword(keyword,pageNumber,pageSize,sortBy,orderBy);
        return new ResponseEntity<>(productResponse,HttpStatus.NOT_FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO product,@PathVariable Long productId){
        ProductDTO productDTO = productService.updateProduct(product, productId);

        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
        String status = productService.deleteProduct(productId);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }

    @PutMapping("admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam MultipartFile image) throws IOException {
        ProductDTO updateProduct = productService.updateImage(productId,image);
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }


}
