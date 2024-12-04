package org.web.shop.app.app.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.web.shop.app.app.model.Product;
import org.web.shop.app.app.model.User;
import org.web.shop.app.app.service.CategoryService;
import org.web.shop.app.app.service.ProductService;
import org.web.shop.app.app.service.SupplierService;
import org.web.shop.app.app.service.impl.UserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final SupplierService supplierService;
    private final CategoryService categoryService;
    private final UserService userService;

    public AdminController(ProductService productService, SupplierService supplierService, CategoryService categoryService, UserService userService) {
        this.productService = productService;
        this.supplierService = supplierService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin-user-list";
    }

    @PostMapping("/users/{id}/makeAdmin")
    public String makeAdmin(@PathVariable Integer id) {
        userService.changeUserRole(id, "ADMIN");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/makeUser")
    public String makeUser(@PathVariable Integer id) {
        userService.changeUserRole(id, "USER");
        return "redirect:/admin/users";
    }

    @GetMapping
    public String admin() {
        return "admin";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        productService.saveProductFromFile(file);
        return "redirect:/shop/home";
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() {
        String filePath = "dbExport.csv";

        productService.createDBFile(filePath);

        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] fileContent = inputStream.readAllBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", file.getName());
            headers.setContentLength(file.length());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<Product> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        model.addAttribute("query", query);
        return "admin-product-list";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/products")
    public String showProducts(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "admin-product-list";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable Integer id, Model model) {
        Product product = productService.findProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("suppliers", supplierService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "product-edit";
    }

    @GetMapping("/new")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("suppliers", supplierService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "product-create";
    }

    @PostMapping("/new")
    public String createProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/update")
    public String updateProduct(@PathVariable Integer id, @ModelAttribute Product product) {
        productService.updateProduct(id, product);
        return "redirect:/admin/products";
    }
}