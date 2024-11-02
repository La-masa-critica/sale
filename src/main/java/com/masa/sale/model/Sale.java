package com.masa.sale.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sale")
@Accessors(chain = true)
public class Sale{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long profileId;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private SaleStatus status;
    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;
    private String comments;

    @Builder.Default
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<SaleDetails> saleDetails = new HashSet<>();
}
