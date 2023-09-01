package com.metalancer.backend.users.repository;

import com.metalancer.backend.products.entity.Products;
import com.metalancer.backend.users.entity.CartEntity;
import com.metalancer.backend.users.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {

    Page<CartEntity> findAllByUser(User user, Pageable pageable);

    int countAllByUser(User user);

    void deleteAllByUser(User user);

    Optional<CartEntity> findByUserAndProducts(User user, Products products);

    Optional<CartEntity> findByIdAndUser(Long id, User user);
}
