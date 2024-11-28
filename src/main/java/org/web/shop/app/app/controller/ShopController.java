package org.web.shop.app.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.web.shop.app.app.model.Product;
import org.web.shop.app.app.repository.ProductRepository;
import org.web.shop.app.app.service.ProductService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    public ShopController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @GetMapping("/home")
    public String getItems(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<Product> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        model.addAttribute("query", query);
        return "product-list";
    }

    @GetMapping("/products")
    public String getProducts(@RequestParam(required = false) String sort, Model model) {
        List<Product> products = productService.sortProducts(sort);
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Integer id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product-detail";
        }
        return "redirect:/shop/home";
    }


}
