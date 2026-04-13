package pe.inpe.ms_traslado.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.inpe.ms_traslado.dto.CapacidadSedeDTO;
import pe.inpe.ms_traslado.dto.GenericResponseDTO;
import pe.inpe.ms_traslado.dto.SedeDisponibilidadRemotoDTO;
import pe.inpe.ms_traslado.dto.SedeResponseDTO;

@FeignClient(name = "ms-Infraestructura-penitenciaria")
public interface MsInstitucionesClient {

    @GetMapping("/api/v1/instituciones/sedes/{id}")
    GenericResponseDTO<SedeResponseDTO> obtenerSede(@PathVariable("id") Long id);

    @GetMapping("/api/v1/instituciones/sedes/{id}/capacidad")
    GenericResponseDTO<CapacidadSedeDTO> obtenerCapacidadSede(@PathVariable("id") Long id);

    @GetMapping("/api/v1/instituciones/sedes/{id}/disponibilidad")
    GenericResponseDTO<Boolean> verificarDisponibilidad(@PathVariable("id") Long id);
}
