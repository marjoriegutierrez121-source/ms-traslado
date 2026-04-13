package pe.inpe.ms_traslado.service;

import pe.inpe.ms_traslado.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface TrasladoService {

    TrasladoResponseDTO registrar(TrasladoRequestDTO dto);

    List<TrasladoResponseDTO> listarConFiltros(Long estado, Long sedeOrigen,
                                               Long sedeDestino,
                                               LocalDateTime fechaInicio,
                                               LocalDateTime fechaFin);

    TrasladoResponseDTO obtenerPorId(Long id);

    TrasladoResponseDTO actualizar(Long id, TrasladoRequestDTO dto);

    TrasladoResponseDTO cambiarEstado(Long id, EstadoUpdateDTO dto);

    TrasladoResponseDTO registrarLlegada(Long id, LlegadaRegistroDTO dto);

    // ── CONSULTAS ─────────────────────────────────────────────────────────────
    List<TrasladoResponseDTO> listarPorInterno(Long idInterno);

    List<TrasladoResponseDTO> listarPorSedeOrigen(Long idSede);

    List<TrasladoResponseDTO> listarPorSedeDestino(Long idSede);

    List<TrasladoResponseDTO> listarEnCurso();

    // ── VALIDACIONES (orquestación) ───────────────────────────────────────────
    ValidacionTrasladoActivoDTO validarTrasladosActivos(Long idInterno);

    ValidacionCapacidadDTO validarCapacidadSede(Long idSede);
}
