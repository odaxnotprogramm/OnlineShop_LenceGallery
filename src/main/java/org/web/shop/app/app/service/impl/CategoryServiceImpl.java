package org.web.shop.app.app.service.impl;

import org.springframework.stereotype.Service;
import org.web.shop.app.app.model.Category;
import org.web.shop.app.app.repository.CategoryRepository;
import org.web.shop.app.app.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
