package pe.inpe.ms_traslado.service;

import pe.inpe.ms_traslado.dto.TrasladoCustodiaRequestDTO;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaResponseDTO;
import pe.inpe.ms_traslado.dto.TrasladoResponseDTO;

import java.util.List;

public interface TrasladoCustodiaService {
    TrasladoCustodiaResponseDTO addCustodia(Long idTraslado, TrasladoCustodiaRequestDTO requestDto);

    void removeCustodia(Long idTraslado, Long idPersonal);

    List<TrasladoCustodiaResponseDTO> getCustodiasXTraslado(Long idTraslado);

    List<TrasladoResponseDTO> getTrasladosXCustodio(Long idPersonal);
}
