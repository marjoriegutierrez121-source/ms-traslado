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

@Mapper(componentModel = "spring", uses = {TrasladoCustodiaMapper.class})
public interface TrasladoMapper {

    @Mapping(target = "idTraslado", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "registrationUser", ignore = true)
    @Mapping(target = "lastModificationDate", ignore = true)
    @Mapping(target = "lastModificationUser", ignore = true)
    @Mapping(target = "custodias", ignore = true)
    Traslado toEntity(TrasladoRequestDTO requestDTO);

    @Mapping(source = "idTraslado", target = "idTraslado")
    @Mapping(source = "idInterno", target = "idInterno")
    @Mapping(source = "sedeOrigenId", target = "sedeOrigenId")
    @Mapping(source = "sedeDestinoId", target = "sedeDestinoId")
    @Mapping(source = "causaId", target = "causaId")
    @Mapping(source = "idResolucion", target = "idResolucion")
    @Mapping(source = "fechaTraslado", target = "fechaTraslado")
    @Mapping(source = "fechaLlegada", target = "fechaLlegada")
    @Mapping(source = "estadoTrasladoId", target = "estadoTrasladoId")
    @Mapping(source = "observaciones", target = "observaciones")
    @Mapping(target = "estadoNombre", ignore = true)
    @Mapping(target = "custodias", source = "custodias", qualifiedByName = "mapCustodias")
    TrasladoResponseDTO toResponseDTO(Traslado entity);

    @Mapping(target = "idTraslado", ignore = true)
    @Mapping(target = "estadoTrasladoId", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "registrationUser", ignore = true)
    @Mapping(target = "lastModificationDate", ignore = true)
    @Mapping(target = "lastModificationUser", ignore = true)
    @Mapping(target = "custodias", ignore = true)
    void updateEntity(TrasladoRequestDTO requestDTO, @MappingTarget Traslado entity);

    List<TrasladoResponseDTO> toResponseDTOList(List<Traslado> entities);

    @Named("mapCustodias")
    default List<TrasladoCustodiaResponseDTO> mapCustodias(List<TrasladoCustodia> custodias) {
        if (custodias == null) {
            return List.of();
        }
        return custodias.stream()
                .map(this::toCustodiaResponseDTO)
                .toList();
    }

    default TrasladoCustodiaResponseDTO toCustodiaResponseDTO(TrasladoCustodia entity) {
        if (entity == null) {
            return null;
        }
        return TrasladoCustodiaResponseDTO.builder()
                .idTrasladoCustodia(entity.getIdTrasladoCustodia())
                .idTraslado(entity.getTraslado() != null ? entity.getTraslado().getIdTraslado() : null)
                .idPersonal(entity.getIdPersonal())
                .rolCustodiaId(entity.getRolCustodiaId())
                .build();
    }
}