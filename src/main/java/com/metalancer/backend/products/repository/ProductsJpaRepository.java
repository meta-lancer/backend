package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.entity.Products;
import com.metalancer.backend.users.entity.Creator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsJpaRepository extends JpaRepository<Products, Long> {

    Page<Products> findAllByCreator(Creator creator, Pageable pageable);

    Page<Products> findAllByCreatorAndStatus(Creator creator, DataStatus status, Pageable pageable);
}
