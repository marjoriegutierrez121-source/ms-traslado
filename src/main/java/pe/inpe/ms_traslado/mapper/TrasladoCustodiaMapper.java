package pe.inpe.ms_traslado.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaRequestDTO;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaResponseDTO;
import pe.inpe.ms_traslado.entity.TrasladoCustodia;

@Mapper(componentModel = "spring")
public interface TrasladoCustodiaMapper {
    @Mapping(target = "idTrasladoCustodia", ignore = true)
    @Mapping(target = "traslado", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "registrationUser", ignore = true)
    @Mapping(target = "lastModificationDate", ignore = true)
    @Mapping(target = "lastModificationUser", ignore = true)
    TrasladoCustodia toEntity(TrasladoCustodiaRequestDTO dto);

    TrasladoCustodiaResponseDTO toDTO(TrasladoCustodia entity);
}
