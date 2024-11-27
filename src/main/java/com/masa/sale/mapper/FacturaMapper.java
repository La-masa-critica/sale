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
    @Mapping(source = "numeroFactura", target = "id")
    @Mapping(source = "fecha", target = "date")
    @Mapping(source = "clienteNombre", target = "customer.name")
    @Mapping(source = "detalles", target = "items")
    @Mapping(source = "total", target = "total")
    @Mapping(target = "status", constant = "Done")  // Asigna "Done" como valor constante para status

    FacturaDTO facturaExternaToFacturaDTO(FacturaExterna facturaExterna);

    // Mapeo de DetalleExterno a ExtItemDTO
    @Mapping(source = "productoId", target = "id")
    @Mapping(source = "productoNombre", target = "name")
    @Mapping(source = "precioUnitario", target = "price")
    @Mapping(source = "cantidad", target = "quantity")
    ExtItemDTO detalleExternoToExtItemDTO(DetalleExterno detalleExterno);

    // Método para convertir una lista de DetalleExterno a lista de ExtItemDTO
    List<ExtItemDTO> detalleExternosToExtItemDTOs(List<DetalleExterno> detallesExterno);
}