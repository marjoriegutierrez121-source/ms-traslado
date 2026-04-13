package pe.inpe.ms_traslado.mapper.client;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.inpe.ms_traslado.dto.ParameterTableResponseDTO;
import pe.inpe.ms_traslado.dto.ParametroResumenDTO;

@Mapper(componentModel = "spring")
public interface ICoreClientMapper {

    @Mapping(source = "id", target = "idParametro")
    @Mapping(source = "codigo", target = "codigoParametro")
    @Mapping(source = "valor", target = "valorParametro")
    @Mapping(source = "descripcion", target = "descripcionParametro")
    ParametroResumenDTO toParametroResumenDTO(ParameterTableResponseDTO parametro);

    default String getValorParametro(ParameterTableResponseDTO parametro) {
        return parametro != null ? parametro.getValor() : null;
    }

    default String getDescripcionParametro(ParameterTableResponseDTO parametro) {
        return parametro != null ? parametro.getDescripcion() : null;
    }
}
