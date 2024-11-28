package org.web.shop.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.shop.app.app.model.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByTitle(String title);
    Optional<Product> findById(Integer id);
}
