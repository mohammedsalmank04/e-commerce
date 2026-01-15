package com.practice.ecommerce.controller;

import com.practice.ecommerce.config.AppConstants;
import com.practice.ecommerce.model.Category;
import com.practice.ecommerce.payload.CategoryDTO;
import com.practice.ecommerce.payload.CategoryResponse;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.practice.ecommerce.service.CategoryService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam(defaultValue = AppConstants.pageNumber, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.pageSize,required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_CATEGORY_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortOrder
    ) {

        CategoryResponse categoryResponse = categoryService.getAllCategory(pageNumber,pageSize,sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {


        CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);

    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {


        CategoryDTO UpdatedcategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(UpdatedcategoryDTO, HttpStatus.OK);

    }


}
