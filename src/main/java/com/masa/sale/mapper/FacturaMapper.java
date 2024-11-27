package com.masa.sale.mapper;
import com.masa.sale.dto.ExtItemDTO;
import com.masa.sale.dto.FacturaDTO;
import com.masa.sale.model.DetalleExterno;
import com.masa.sale.model.FacturaExterna;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FacturaMapper {

    // Instancia del Mapper
    FacturaMapper INSTANCE = Mappers.getMapper(FacturaMapper.class);

    // Mapeo de FacturaExterna a FacturaDTO
    @Mapping(source = "numero_factura", target = "id")
    @Mapping(source = "fecha", target = "date")
    @Mapping(source = "cliente_nombre", target = "customer.name")
    @Mapping(source = "cliente_documento", target = "customer.id")
    @Mapping(source = "detalles", target = "items")
    @Mapping(source = "total", target = "total")
    @Mapping(target = "status", constant = "COMPLETED")  // Asigna "Done" como valor constante para status
    FacturaDTO facturaExternaToFacturaDTO(FacturaExterna facturaExterna);

    // Mapeo de DetalleExterno a ExtItemDTO
    @Mapping(source = "producto_id", target = "id")
    @Mapping(source = "producto_nombre", target = "name")
    @Mapping(source = "precio_unitario", target = "price")
    @Mapping(source = "cantidad", target = "quantity")
    ExtItemDTO detalleExternoToExtItemDTO(DetalleExterno detalleExterno);

    // Método para convertir una lista de DetalleExterno a lista de ExtItemDTO
    List<ExtItemDTO> detalleExternosToExtItemDTOs(List<DetalleExterno> detallesExterno);
}