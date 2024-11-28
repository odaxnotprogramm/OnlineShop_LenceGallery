package org.web.shop.app.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.web.shop.app.app.model.Image;
import org.web.shop.app.app.model.User;
import org.web.shop.app.app.model.enums.Role;
import org.web.shop.app.app.repository.ImageRepository;
import org.web.shop.app.app.repository.UserRepository;

import javax.transaction.Transactional;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public boolean createUser(User input) {
        User existingUser = userRepository.findByUsername(input.getUsername());
        if (existingUser != null) {
            log.warn("User with username {} already exists.", input.getUsername());
            return false;
        }

        User user = new User();
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setActive(true);
        user.getRoles().add(Role.USER);

        Image image = new Image();
        image.setUrl("https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3383.jpg");
        image.setUser(user);
        user.setImage(image);

        userRepository.save(user);
        imageRepository.save(image);

        log.info("User created: {}", user.getUsername());
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
