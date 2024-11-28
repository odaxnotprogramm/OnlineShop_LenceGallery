package org.web.shop.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.shop.app.app.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryByName(String value);
}
