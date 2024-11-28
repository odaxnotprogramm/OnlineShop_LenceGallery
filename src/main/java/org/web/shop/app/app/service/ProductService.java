package org.web.shop.app.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.web.shop.app.app.model.Product;

import java.util.List;

@Service
public interface ProductService {
    List<Product> findAllProducts();

    Product saveProduct(Product product);

    Product findProductById(Integer id);

    void deleteProduct(Integer id);

    void saveProductFromFile(MultipartFile file);

    void createDBFile(String file);

    List<Product> searchProducts(String query);

    void updateProduct(Integer id, Product product);

    List<Product> sortProducts(String sort);

    void save(Product product);
}
