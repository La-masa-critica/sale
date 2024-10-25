package com.masa.sell.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;


@Entity(name = "sale")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Sale{
    @Id
    private Long id;
    private Long profileId;
    private Timestamp date;
    private String status;
    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;
    private String comments;

    @OneToMany(mappedBy = "Sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SaleDetails> saleDetails = new HashSet<>();
}
