package pe.inpe.ms_traslado.mapper.client;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.inpe.ms_traslado.dto.CapacidadSedeDTO;
import pe.inpe.ms_traslado.dto.CapacidadSedeResumenDTO;
import pe.inpe.ms_traslado.dto.SedeResponseDTO;
import pe.inpe.ms_traslado.dto.SedeResumenDTO;

@Mapper(componentModel = "spring")
public interface IInstitucionClientMapper {

    @Mapping(source = "id", target = "idSede")
    @Mapping(source = "nombre", target = "nombreSede")
    @Mapping(source = "codigo", target = "codigoSede")
    @Mapping(source = "nivelSeguridad", target = "nivelSeguridad")
    @Mapping(source = "activo", target = "activo")
    SedeResumenDTO toSedeResumenDTO(SedeResponseDTO sede);

    @Mapping(source = "idSede", target = "idSede")
    @Mapping(source = "capacidadMaxima", target = "capacidadMaxima")
    @Mapping(source = "ocupacionActual", target = "ocupacionActual")
    @Mapping(source = "cuposDisponibles", target = "cuposDisponibles")
    CapacidadSedeResumenDTO toCapacidadSedeResumenDTO(CapacidadSedeDTO capacidad);

    default String getNombreSede(SedeResponseDTO sede) {
        return sede != null ? sede.getNombre() : null;
    }

    default Boolean isSedeActiva(SedeResponseDTO sede) {
        return sede != null && Boolean.TRUE.equals(sede.getActivo());
    }
}