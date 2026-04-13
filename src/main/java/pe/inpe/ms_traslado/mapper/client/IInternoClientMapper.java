package pe.inpe.ms_traslado.mapper.client;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.inpe.ms_traslado.dto.*;

@Mapper(componentModel = "spring")
public interface IInternoClientMapper {

    @Mapping(source = "idInterno", target = "idInterno")
    @Mapping(source = "idPersona", target = "idPersona")
    @Mapping(source = "codigoInterno", target = "codigoInterno")
    @Mapping(source = "estado", target = "activo")
    InternoResumenDTO toInternoResumenDTO(InternoResponseDTO interno);

    @Mapping(source = "idInternoUbicacion", target = "idInternoUbicacion")
    @Mapping(source = "idInterno", target = "idInterno")
    @Mapping(source = "idInstitutoSede", target = "idSede")
    @Mapping(source = "nombreSede", target = "nombreSede")
    @Mapping(source = "activo", target = "ubicacionActiva")
    InternoUbicacionResumenDTO toUbicacionResumenDTO(InternoUbicacionResponseDTO ubicacion);

    @Mapping(source = "idClasificacionInterno", target = "idClasificacion")
    @Mapping(source = "nivelSeguridadId", target = "nivelSeguridadId")
    @Mapping(source = "nivelSeguridadDescripcion", target = "nivelSeguridadDescripcion")
    @Mapping(source = "activo", target = "clasificacionActiva")
    ClasificacionInternoResumenDTO toClasificacionResumenDTO(ClasificacionInternoResponseDTO clasificacion);

    default Boolean isInternoActivo(InternoResponseDTO interno) {
        return interno != null && Boolean.TRUE.equals(interno.getEstado());
    }

    default String getCodigoInterno(InternoResponseDTO interno) {
        return interno != null ? interno.getCodigoInterno() : null;
    }
}