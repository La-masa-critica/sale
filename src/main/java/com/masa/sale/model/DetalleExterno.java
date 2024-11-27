package com.masa.sale.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleExterno {
    private String productoId;
    private String productoNombre;
    private Integer cantidad;
    private String precioUnitario;
    private String precioTotal;
}
