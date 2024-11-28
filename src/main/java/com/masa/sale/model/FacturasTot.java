package com.masa.sale.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturasTot {
    private String numero_factura;
    private String cliente_nombre;
    private String fecha;
    private String total;
}