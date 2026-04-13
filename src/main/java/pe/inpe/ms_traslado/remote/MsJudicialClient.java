package pe.inpe.ms_traslado.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.inpe.ms_traslado.dto.ExpedienteResponseDTO;
import pe.inpe.ms_traslado.dto.GenericResponseDTO;
import pe.inpe.ms_traslado.dto.MandatoActivoDTO;
import pe.inpe.ms_traslado.dto.RestriccionesTrasladoDTO;

import java.util.List;

@FeignClient(name = "ms-judicial", url = "${ms.judicial.url:http://localhost:8084}")
public interface MsJudicialClient {

    @GetMapping("/api/v1/judicial/interno/{idInterno}/mandato-activo")
    GenericResponseDTO<MandatoActivoDTO> obtenerMandatoActivo(@PathVariable("idInterno") Long idInterno);

    @GetMapping("/api/v1/judicial/interno/{idInterno}/restricciones-traslado")
    GenericResponseDTO<RestriccionesTrasladoDTO> obtenerRestriccionesTraslado(@PathVariable("idInterno") Long idInterno);

    @GetMapping("/api/v1/judicial/expedientes/interno/{idInterno}")
    GenericResponseDTO<List<ExpedienteResponseDTO>> listarExpedientesPorInterno(@PathVariable("idInterno") Long idInterno);
}
