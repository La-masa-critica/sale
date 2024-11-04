package com.masa.sale.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

import lombok.*;

@Entity(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Cart implements Serializable {
    @Id
    private Long id;
    private Boolean enabled;

    @Builder.Default
    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<CartItem> cartItems = new HashSet<>();

}
