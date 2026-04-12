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
    @Mapping(target = "traslado", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "registrationUser", ignore = true)
    @Mapping(target = "lastModificationDate", ignore = true)
    @Mapping(target = "lastModificationUser", ignore = true)
    TrasladoCustodia toEntity(TrasladoCustodiaRequestDTO requestDTO);

    // TRASLADO CUSTODIA: Entity → ResponseDTO
    @Mapping(source = "idTrasladoCustodia", target = "idTrasladoCustodia")
    @Mapping(source = "traslado.idTraslado", target = "idTraslado")
    @Mapping(source = "idPersonal", target = "idPersonal")
    @Mapping(source = "rolCustodiaId", target = "rolCustodiaId")
    TrasladoCustodiaResponseDTO toResponseDTO(TrasladoCustodia entity);

    // TRASLADO CUSTODIA: Lista de Entities → Lista de ResponseDTO
    List<TrasladoCustodiaResponseDTO> toResponseDTOList(List<TrasladoCustodia> entities);
}
