package com.masa.sale.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaExterna {

    private Integer numero_factura;
    private String fecha;
    private String vendedor_nombre;
    private String cliente_nombre;
    private String cliente_documento;
    private List<DetalleExterno> detalles;
    private String total;
    }
