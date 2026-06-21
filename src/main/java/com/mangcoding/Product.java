package com.mangcoding;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class Product extends PanacheEntity {
    
    @NotBlank(message = "Product name cannot be blank")
    public String name;
    
    public String description;
    
    @Positive(message = "Price must be positive")
    public BigDecimal price;
    
    public int stockQuantity;
}
