package com.masa.sell.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "sale_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SaleDetails{
    @Id
    private Long id;
    private Long saleId;
    private Long itemId;
    private Integer quantity;
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;
}
