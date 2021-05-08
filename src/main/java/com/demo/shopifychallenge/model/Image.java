package com.demo.shopifychallenge.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Optional;

@Entity
@ToString
@EqualsAndHashCode
public class Image extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String imageurl;
    private double imagePrice;

    @Override
    public Instant getCreatedAt() {
        return super.getCreatedAt();
    }

    @Override
    public void setCreatedAt(Instant createdAt) {
        super.setCreatedAt(createdAt);
    }

    @Override
    public Instant getUpdateAt() {
        return super.getUpdateAt();
    }

    @Override
    public void setUpdateAt(Instant updateAt) {
        super.setUpdateAt(updateAt);
    }

    public Image() {
    }

    public Image(String name,
                 String description,
                 String imageUrl,
                 double imagePrice) {
        this.name = name;
        this.description = description;
        this.imageurl = imageUrl;
        this.imagePrice = imagePrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Optional<String> getImageurl() {

        return Optional.ofNullable(imageurl);
    }

    public void setImageurl(String imageUrl) {
        this.imageurl = imageUrl;
    }

    public double getImagePrice() {
        return imagePrice;
    }

    public void setImagePrice(double imagePrice) {
        this.imagePrice = imagePrice;
    }
}
