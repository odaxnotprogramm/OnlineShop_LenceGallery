package org.web.shop.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.shop.app.app.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
