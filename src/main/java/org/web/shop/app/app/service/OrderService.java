package org.web.shop.app.app.service;

import org.springframework.stereotype.Service;
import org.web.shop.app.app.model.Order;
import org.web.shop.app.app.model.Product;
import org.web.shop.app.app.model.User;

import java.util.Set;

@Service
public interface OrderService {

    Order createOrder(Set<Product> products, User user, String deliveryAddress, String deliveryTime);

}
