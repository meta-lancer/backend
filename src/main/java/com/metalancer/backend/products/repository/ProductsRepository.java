package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.domain.Products;
import com.metalancer.backend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products, Long> {

    Page<Products> findAllByCreator(User creator, Pageable pageable);

    Page<Products> findAllByCreatorAndStatus(User creator, DataStatus status, Pageable pageable);
}
