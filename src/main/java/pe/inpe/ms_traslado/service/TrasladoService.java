package pe.inpe.ms_traslado.service;

import pe.inpe.ms_traslado.dto.TrasladoCustodiaResponseDTO;
import pe.inpe.ms_traslado.dto.TrasladoRequestDTO;
import pe.inpe.ms_traslado.dto.TrasladoResponseDTO;

import java.util.List;

public interface TrasladoService {

    TrasladoResponseDTO addTraslado(TrasladoRequestDTO trasladoDto);
    List<TrasladoResponseDTO> getTrasladoXEstado(Boolean estado);
    TrasladoResponseDTO getTrasladoXId(Long id);
    List<TrasladoResponseDTO> getTrasladoXIdInterno(Long idInterno);
    TrasladoResponseDTO putTraslado(Long id, TrasladoRequestDTO trasladoDto);
    List<TrasladoCustodiaResponseDTO> getCustodiaXId(Long idTraslado);
}
