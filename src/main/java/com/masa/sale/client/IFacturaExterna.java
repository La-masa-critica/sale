package com.masa.sale.client;

import com.masa.sale.model.FacturaExterna;
import com.masa.sale.model.FacturasTot;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "fromagesClient", url = "https://back-fromages.azurewebsites.net")
public interface IFacturaExterna {
    @GetMapping("/reporte/facturas/")
    List<FacturasTot> obtenerFacturas(
            @RequestParam("option") String option, // "facturas" fijo
            @RequestParam("fecha_inicio") String fechaInicio, // "2024-11-01" fijo
            @RequestParam("fecha_fin") String fechaFin, // "2024-11-30" fijo
            @RequestParam("sucursal") String sucursal // "Sucursal Fromages" fijo
    );

    @GetMapping("/reporte/factura-detalle/{id}")
    FacturaExterna getFacturaDetalle(@PathVariable("id") String id);

}
