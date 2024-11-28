package org.web.shop.app.app.service.impl;

import org.springframework.stereotype.Service;
import org.web.shop.app.app.model.Order;
import org.web.shop.app.app.model.Product;
import org.web.shop.app.app.model.User;
import org.web.shop.app.app.repository.OrderRepository;
import org.web.shop.app.app.repository.ProductRepository;
import org.web.shop.app.app.service.OrderService;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order createOrder(Set<Product> cartProducts, User user) {
        Order order = new Order();
        order.setUser(user);

        Set<Product> orderProducts = new HashSet<>();
        for (Product product : cartProducts) {
            Product managedProduct = productRepository.findById(product.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            orderProducts.add(managedProduct);
        }

        order.setProducts(orderProducts);
        return orderRepository.save(order);
    }

    @Override
    public Order saveOrder(Order order) {
        Set<Product> updatedProducts = new LinkedHashSet<>();

        for (Product product : order.getProducts()) {
            if (product.getId() != null) {
                Product managedProduct = productRepository.findById(product.getId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                updatedProducts.add(managedProduct);
            }
        }

        order.setProducts(updatedProducts);

        return orderRepository.save(order);
    }
}
