package pe.inpe.ms_traslado.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pe.inpe.ms_traslado.dto.TrasladoRequestDTO;
import pe.inpe.ms_traslado.dto.TrasladoResponseDTO;
import pe.inpe.ms_traslado.entity.Traslado;

@Mapper(componentModel = "spring", uses = {TrasladoCustodiaMapper.class})
public interface TrasladoMapper {

    @Mapping(target = "idTraslado", ignore = true)
    @Mapping(target = "estado", constant = "true")
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "registrationUser", ignore = true)
    @Mapping(target = "lastModificationDate", ignore = true)
    @Mapping(target = "lastModificationUser", ignore = true)
    @Mapping(target = "custodias", ignore = true) // 👈 se maneja en el Service
    Traslado toEntity(TrasladoRequestDTO dto);

    TrasladoResponseDTO toDTO(Traslado entity);

    @Mapping(target = "idTraslado", ignore = true)
    @Mapping(target = "idInterno", ignore = true) // no se cambia el interno
    @Mapping(target = "estado", ignore = true)    // el estado se maneja aparte
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "registrationUser", ignore = true)
    @Mapping(target = "lastModificationDate", ignore = true)
    @Mapping(target = "lastModificationUser", ignore = true)
    @Mapping(target = "custodias", ignore = true) // se maneja en el Service
    void updateEntity(TrasladoRequestDTO dto, @MappingTarget Traslado entity);
}