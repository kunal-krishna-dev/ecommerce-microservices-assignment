package com.koerber.inventory.repository;

import com.koerber.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
       SELECT DISTINCT p
       FROM Product p 
       LEFT JOIN FETCH p.batches b
       WHERE p.productId = :productId
       """)
    Optional<Product> findByProductId(@Param("productId") Long productId);
}
