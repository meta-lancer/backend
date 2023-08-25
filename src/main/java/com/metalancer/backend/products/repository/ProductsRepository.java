package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.domain.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    
}
