package pe.inpe.ms_traslado.service;

import pe.inpe.ms_traslado.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface TrasladoService {

    TrasladoResponseDTO addTraslado(TrasladoRequestDTO trasladoDto);

    List<TrasladoResponseDTO> getTrasladosConFiltros(Long estado, Long sedeOrigen, Long sedeDestino,
                                                     LocalDateTime fechaInicio, LocalDateTime fechaFin);

    TrasladoResponseDTO getTrasladoXId(Long id);

    List<TrasladoResponseDTO> getTrasladosXIdInterno(Long idInterno);

    TrasladoResponseDTO putTraslado(Long id, TrasladoRequestDTO trasladoDto);

    TrasladoResponseDTO patchCambiarEstado(Long id, CambioEstadoDTO cambioEstadoDto);

    TrasladoResponseDTO patchRegistrarLlegada(Long id, RegistroLlegadaDTO registroLlegadaDto);

    List<TrasladoResponseDTO> getTrasladosXSedeOrigen(Long idSede);

    List<TrasladoResponseDTO> getTrasladosXSedeDestino(Long idSede);

    List<TrasladoResponseDTO> getTrasladosEnCurso();

    ValidacionTrasladoDTO validarTrasladoActivo(Long idInterno);

    ValidacionCapacidadDTO verificarCapacidadSede(Long idSede);
}
