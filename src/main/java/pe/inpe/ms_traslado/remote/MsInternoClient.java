package pe.inpe.ms_traslado.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.inpe.ms_traslado.dto.*;


@FeignClient(name = "ms-interno", url = "http://localhost:8084")
public interface MsInternoClient {

    @GetMapping("/api/v1/interno/{id}")
    GenericResponseDTO<InternoResponseDTO> obtenerInterno(@PathVariable("id") Long id);

    @GetMapping("/api/v1/interno/{id}/puede-ser-trasladado")
    GenericResponseDTO<Boolean> puedeSerTrasladado(@PathVariable("id") Long id);

    @GetMapping("/api/v1/interno/{id}/motivo-restriccion")
    GenericResponseDTO<String> obtenerMotivoRestriccionTraslado(@PathVariable("id") Long id);

    @GetMapping("/api/v1/interno/{id}/ubicacion-actual")
    GenericResponseDTO<InternoUbicacionResponseDTO> obtenerUbicacionActual(@PathVariable("id") Long id);

    @GetMapping("/api/v1/interno/{id}/clasificacion-actual")
    GenericResponseDTO<ClasificacionInternoResponseDTO> obtenerClasificacionActual(@PathVariable("id") Long id);
}
