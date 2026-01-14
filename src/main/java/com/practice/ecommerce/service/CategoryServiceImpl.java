package com.practice.ecommerce.service;

import com.practice.ecommerce.Repositories.CategoryRepository;
import com.practice.ecommerce.exceptions.APIExceptions;
import com.practice.ecommerce.exceptions.ResourceNotFoundException;
import com.practice.ecommerce.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{



    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty())
            throw new APIExceptions("There are not Category present");

        return categories;
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null)
            throw new APIExceptions("Category with name: " + category.getCategoryName() + " already Exists");

        categoryRepository.save(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));

        categoryRepository.delete(category);
        return "Category with categoryID: " + categoryId + " Deleted";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        Category oldCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));

        oldCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(oldCategory);


    }
}
