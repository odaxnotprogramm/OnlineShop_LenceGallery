package org.web.shop.app.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.shop.app.app.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    Image getImageById(int i);
}
