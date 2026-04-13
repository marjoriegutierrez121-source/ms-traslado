package pe.inpe.ms_traslado.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaResponseDTO;
import pe.inpe.ms_traslado.dto.TrasladoRequestDTO;
import pe.inpe.ms_traslado.dto.TrasladoResponseDTO;
import pe.inpe.ms_traslado.entity.Traslado;
import pe.inpe.ms_traslado.entity.TrasladoCustodia;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrasladoMapper {

    @Mapping(target = "idTraslado",            ignore = true)
    @Mapping(target = "estadoTrasladoId",       ignore = true)
    @Mapping(target = "fechaLlegada",           ignore = true)
    @Mapping(target = "registrationDate",       ignore = true)
    @Mapping(target = "registrationUser",       ignore = true)
    @Mapping(target = "lastModificationDate",   ignore = true)
    @Mapping(target = "lastModificationUser",   ignore = true)
    Traslado toEntity(TrasladoRequestDTO dto);

    TrasladoResponseDTO toDto(Traslado entity);

    List<TrasladoResponseDTO> toDtoList(List<Traslado> entities);

    @Mapping(target = "idTraslado",            ignore = true)
    @Mapping(target = "estadoTrasladoId",       ignore = true)
    @Mapping(target = "fechaLlegada",           ignore = true)
    @Mapping(target = "registrationDate",       ignore = true)
    @Mapping(target = "registrationUser",       ignore = true)
    @Mapping(target = "lastModificationDate",   ignore = true)
    @Mapping(target = "lastModificationUser",   ignore = true)
    void updateEntityFromDto(TrasladoRequestDTO dto, @MappingTarget Traslado entity);
}