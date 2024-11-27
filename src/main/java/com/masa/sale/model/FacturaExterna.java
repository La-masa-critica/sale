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

    private Integer numeroFactura;
    private String fecha;
    private String vendedorNombre;
    private String clienteNombre;
    private String clienteDocumento;
    private List<DetalleExterno> detalles;
    private String total;



    }


