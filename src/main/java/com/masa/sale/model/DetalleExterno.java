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
    private String producto_id;
    private String producto_nombre;
    private Integer cantidad;
    private String precio_unitario;
    private String precio_total;
}
