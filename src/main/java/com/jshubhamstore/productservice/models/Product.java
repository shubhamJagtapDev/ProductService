package com.jshubhamstore.productservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity(name = "products")
public class Product extends BaseModel{
    @Column(nullable = false, unique = true)
    private String title;
    private String description;
    private Double price;
    private String imageUrl;
    @ManyToOne
    private Category category;
}