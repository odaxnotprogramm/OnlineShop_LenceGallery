package org.web.shop.app.app.service;

import org.springframework.stereotype.Service;
import org.web.shop.app.app.model.Category;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> findAll();
}
