package org.web.shop.app.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String url;

    public Image(String url) {
        this.url = url;
    }

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private User user;
}
