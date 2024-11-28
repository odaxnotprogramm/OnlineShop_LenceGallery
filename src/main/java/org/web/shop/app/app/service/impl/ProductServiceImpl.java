package org.web.shop.app.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.web.shop.app.app.model.Category;
import org.web.shop.app.app.model.Product;
import org.web.shop.app.app.repository.CategoryRepository;
import org.web.shop.app.app.repository.ProductRepository;
import org.web.shop.app.app.repository.SupplierRepository;
import org.web.shop.app.app.repository.UserRepository;
import org.web.shop.app.app.service.ProductService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository, CategoryRepository categoryRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }


    @Override
    public Product findProductById(Integer id) {
        return productRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void saveProductFromFile(MultipartFile file) {
        entityManager.createNativeQuery("TRUNCATE TABLE user_cart").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE user_favourites").executeUpdate();
        productRepository.deleteAll();
        List<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split(";");
                if (values.length == 7) {
                    Product product = new Product();
                    product.setTitle(values[0]);
                    product.setPrice(Integer.parseInt(values[1]));
                    product.setPhotoUrl(values[2]);
                    product.setCity(values[3]);
                    product.setSupplier(supplierRepository.findSupplierByName(values[4]));

                    String categoryName = values[5];
                    Category category = categoryRepository.findCategoryByName(categoryName);
                    if (category != null) {
                        product.setCategory(category);
                    } else {
                        throw new RuntimeException("Category not found: " + categoryName);
                    }

                    product.setDescription(values[6]);
                    products.add(product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Product product : products) {
            saveProduct(product);
        }
    }

    @Override
    public void createDBFile(String filePath) {
        List<Product> products = productRepository.findAll();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Product product : products) {
                writer.write(product.getTitle() + ";" +
                        product.getPrice() + ";" +
                        product.getPhotoUrl() + ";" +
                        product.getCity() + ";" +
                        product.getSupplier().getName() + ";" +
                        product.getCategory().getName() + ";" +
                        product.getDescription());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findAll().stream()
                .filter(product -> product.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateProduct(Integer id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setTitle(product.getTitle());
        existingProduct.setPhotoUrl(product.getPhotoUrl());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setSupplier(product.getSupplier());
        existingProduct.setCity(product.getCity());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());


        productRepository.save(existingProduct);
    }

    @Override
    public List<Product> sortProducts(String sort) {
        List<Product> products = productRepository.findAll();
        switch (sort) {
            case "price_asc":
                products.sort(Comparator.comparingInt(Product::getPrice));
                break;
            case "price_desc":
                products.sort(Comparator.comparingInt(Product::getPrice).reversed());
                break;
            case "name_asc":
                products.sort(Comparator.comparing(Product::getTitle));
                break;
            case "name_desc":
                products.sort(Comparator.comparing(Product::getTitle).reversed());
                break;
            default:
                break;
        }
        return products;
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }
}
