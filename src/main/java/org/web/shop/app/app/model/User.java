package org.web.shop.app.app.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.web.shop.app.app.model.enums.Role;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "usr")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Product> products = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_cart",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> cart = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_favourites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> favourites = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Image image;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_orders",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Order> orders = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public void addToCart(Product product) {
        cart.add(product);
    }

    public void removeFromCart(Integer productId) {
        cart.removeIf(product -> product.getId().equals(productId));
    }

    public void clearCart() {
        cart.clear();
    }

    public void addToFavourites(Product product) {
        favourites.add(product);
    }

    public void removeFromFavourites(Integer productId) {
        favourites.removeIf(product -> product.getId().equals(productId));
    }

    public void setUrl(String imageUrl) {
        image.setUrl(imageUrl);
    }

    public void addToOrders(Order order) {
        orders.add(order);
    }
}