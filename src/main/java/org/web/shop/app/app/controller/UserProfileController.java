package org.web.shop.app.app.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.web.shop.app.app.model.Image;
import org.web.shop.app.app.model.Order;
import org.web.shop.app.app.model.Product;
import org.web.shop.app.app.model.User;
import org.web.shop.app.app.repository.OrderRepository;
import org.web.shop.app.app.repository.ProductRepository;
import org.web.shop.app.app.repository.UserRepository;
import org.web.shop.app.app.service.OrderService;

import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public UserProfileController(UserRepository userRepository, ProductRepository productRepository, OrderService orderService, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam String username,
                                @RequestParam String imageUrl) {
        User user = userRepository.findByUsername(userDetails.getUsername());

        if (user != null) {
            user.setUsername(username);

            if (user.getImage() == null) {
                user.setImage(new Image(imageUrl));
            } else {
                user.getImage().setUrl(imageUrl);
            }

            userRepository.save(user);
        }

        return "redirect:/profile/user";
    }

    @GetMapping("/user")
    @Transactional
    public String getUserProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null) {
            return "redirect:/error";
        }
        model.addAttribute("user", user);
        return "cabinet";
    }

    @GetMapping("/favourites")
    public String viewFavourites(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername());

        if (user != null) {
            model.addAttribute("favourites", user.getFavourites());
        } else {
            return "redirect:/error";
        }

        return "favourites";
    }

    @PostMapping("/{id}/addToCart")
    public String addToCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer id, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent() && user != null) {
            Product product = optionalProduct.get();
            user.addToCart(product);
            userRepository.save(user);
        }

        return "redirect:/profile/cart";
    }

    @PostMapping("/{id}/addToFavourites")
    @Transactional
    public String addToFavourites(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer id) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent() && user != null) {
            Product product = optionalProduct.get();
            user.addToFavourites(product);
            userRepository.save(user);
        }

        return "redirect:/profile/favourites";
    }

    @PostMapping("/{id}/removeFromFavourites")
    @Transactional
    public String removeFromFavourites(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer id) {
        User user = userRepository.findByUsername(userDetails.getUsername());

        if (user != null) {
            user.removeFromFavourites(id);
            userRepository.save(user);
        }

        return "redirect:/profile/favourites";
    }

    @GetMapping("/cart")
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername());

        if (user != null) {
            model.addAttribute("cart", user.getCart());
            model.addAttribute("totalPrice", calculateTotalPrice(user.getCart()));
        }

        return "cart";
    }

    @PostMapping("/{id}/delete")
    public String removeFromCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer id) {
        User user = userRepository.findByUsername(userDetails.getUsername());

        if (user != null) {
            user.removeFromCart(id);
            userRepository.save(user);
        }

        return "redirect:/profile/cart";
    }

    @PostMapping("/buy")
    public String buy(@AuthenticationPrincipal User currentUser, Model model) {
        Set<Product> cartProducts = currentUser.getCart();
        Order order = orderService.createOrder(cartProducts, currentUser);

        model.addAttribute("order", order);

        return "order";
    }

    @GetMapping("/checkOrder")
    public String confirmOrderPage(@ModelAttribute("order") Order order, Model model) {
        if (order == null) {
            return "redirect:/error";
        }

        model.addAttribute("cart", order.getProducts());
        model.addAttribute("totalPrice", calculateTotalPrice(order.getProducts()));
        return "order";
    }

    @PostMapping("/confirmOrder")
    public String confirmOrder(@RequestParam("address") String address,
                               @RequestParam("deliveryTime") String deliveryTime,
                               @ModelAttribute("order") Order order,
                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());

        if (order != null && user != null) {
            order.setDeliveryAddress(address);
            order.setDeliveryTime(deliveryTime);
            user.addToOrders(order);
            userRepository.save(user);
        }

        return "redirect:/profile/orders";
    }

    @GetMapping("/orders")
    public String viewOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername());

        if (user == null) {
            return "redirect:/error";
        }

        model.addAttribute("orders", user.getOrders());
        return "user-orders";
    }

    private double calculateTotalPrice(Set<Product> cart) {
        return cart.stream().mapToDouble(Product::getPrice).sum();
    }
}