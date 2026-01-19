package com.practice.ecommerce.Repositories;

import com.practice.ecommerce.model.Category;
import com.practice.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryOrderByPriceAsc(Category category);
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageables);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);
    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageable);

    Product findByProductName(String productName);
}
