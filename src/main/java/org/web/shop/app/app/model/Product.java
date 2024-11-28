package org.web.shop.app.app.model;

import lombok.Data;
import org.web.shop.app.app.listener.ProductListener;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Data
@EntityListeners(ProductListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "photo_url")
    private String photoUrl;
    @Column(name = "price")
    private int price;
    @Column(name = "city")
    private String city;
    @Column(name = "description")
    private String description;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}