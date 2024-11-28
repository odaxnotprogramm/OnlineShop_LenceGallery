package org.web.shop.app.app.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;
    private String address;
    private String phone;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
}