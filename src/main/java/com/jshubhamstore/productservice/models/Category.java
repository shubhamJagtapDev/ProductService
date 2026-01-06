package com.jshubhamstore.productservice.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity(name = "categories")
public class Category extends BaseModel{
    private String title;
}