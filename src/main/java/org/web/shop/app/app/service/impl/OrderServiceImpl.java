package org.web.shop.app.app.service.impl;

import org.springframework.stereotype.Service;
import org.web.shop.app.app.model.Order;
import org.web.shop.app.app.model.Product;
import org.web.shop.app.app.model.User;
import org.web.shop.app.app.repository.OrderRepository;
import org.web.shop.app.app.service.OrderService;

import javax.transaction.Transactional;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Set<Product> products, User user, String deliveryAddress, String deliveryTime) {
        Order order = new Order();

        order.setProducts(new LinkedHashSet<>(products));
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);
        order.setDeliveryTime(deliveryTime);

        double totalPrice = products.stream().mapToDouble(Product::getPrice).sum();
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }
}