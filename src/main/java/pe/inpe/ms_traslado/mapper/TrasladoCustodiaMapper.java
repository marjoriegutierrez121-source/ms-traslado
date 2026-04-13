package pe.inpe.ms_traslado.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaRequestDTO;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaResponseDTO;
import pe.inpe.ms_traslado.entity.TrasladoCustodia;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrasladoCustodiaMapper {

    @Mapping(target = "idTrasladoCustodia", ignore = true)
    @Mapping(target = "traslado",           ignore = true)
    TrasladoCustodia toEntity(TrasladoCustodiaRequestDTO dto);

    @Mapping(source = "traslado.idTraslado", target = "idTraslado")
    TrasladoCustodiaResponseDTO toDto(TrasladoCustodia entity);

    List<TrasladoCustodiaResponseDTO> toDtoList(List<TrasladoCustodia> entities);
}
