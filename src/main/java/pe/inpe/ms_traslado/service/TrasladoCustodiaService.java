package pe.inpe.ms_traslado.service;

import pe.inpe.ms_traslado.dto.TrasladoCustodiaRequestDTO;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaResponseDTO;
import pe.inpe.ms_traslado.dto.TrasladoResponseDTO;

import java.util.List;

public interface TrasladoCustodiaService {

    TrasladoCustodiaResponseDTO asignarCustodia(Long idTraslado, TrasladoCustodiaRequestDTO dto);

    void removerCustodia(Long idTraslado, Long idPersonal);

    List<TrasladoCustodiaResponseDTO> listarPorTraslado(Long idTraslado);

    List<TrasladoCustodiaResponseDTO> listarPorPersonal(Long idPersonal);
}
