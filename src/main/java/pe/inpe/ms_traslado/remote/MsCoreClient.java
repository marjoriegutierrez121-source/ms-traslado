package pe.inpe.ms_traslado.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.inpe.ms_traslado.dto.GenericResponseDTO;
import pe.inpe.ms_traslado.dto.ParameterTableResponseDTO;

@FeignClient(name = "ms-core")
public interface MsCoreClient {

    @GetMapping("/api/v1/core/parameters/tipo/{tipo}/codigo/{codigo}")
    GenericResponseDTO<ParameterTableResponseDTO> obtenerParametro(
            @PathVariable("tipo") Integer tipo,
            @PathVariable("codigo") String codigo);

    @GetMapping("/api/v1/core/parameters/tipo/{tipo}/codigo/{codigo}/existe")
    GenericResponseDTO<Boolean> existeParametro(
            @PathVariable("tipo") Integer tipo,
            @PathVariable("codigo") String codigo);

    @GetMapping("/api/v1/core/parameters/{id}")
    GenericResponseDTO<ParameterTableResponseDTO> obtenerParametroPorId(@PathVariable("id") Long id);
}
