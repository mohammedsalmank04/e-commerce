package com.practice.ecommerce.controller;

import com.practice.ecommerce.model.Category;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private List<Category> categories = new ArrayList<>();

    @GetMapping("/public/categories")
    public List<Category> getCategories(){
        return categories;
    }

    @PostMapping("/public/categories")
    public String createCategory(@RequestBody Category category){
        categories.add(category);
        return "Category Added";
    }



}
