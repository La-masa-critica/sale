package com.masa.sell.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "category")
public class Category{
    @Id
    private Long id;
    private String name;
}
