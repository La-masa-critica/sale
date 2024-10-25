package com.masa.sell.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.HashSet;

import lombok.*;

@Entity(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Cart{
    @Id
    private Long id;
    private Long profileId;
    private Boolean enabled;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

}
