package org.web.shop.app.app.listener;

import org.web.shop.app.app.model.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PreRemove;

public class ProductListener {

    @PersistenceContext
    private EntityManager entityManager;

    @PreRemove
    public void onPreRemove(Product product) {
//        entityManager.createNativeQuery("DELETE FROM user_favourites WHERE product_id = :productId")
//                .setParameter("productId", product.getId())
//                .executeUpdate();
//
//        entityManager.createNativeQuery("DELETE FROM user_cart WHERE product_id = :productId")
//                .setParameter("productId", product.getId())
//                .executeUpdate();
    }
}