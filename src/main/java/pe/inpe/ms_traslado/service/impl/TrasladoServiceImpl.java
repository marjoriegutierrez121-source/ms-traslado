package pe.inpe.ms_traslado.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.inpe.ms_traslado.dto.*;
import pe.inpe.ms_traslado.entity.Traslado;
import pe.inpe.ms_traslado.exception.BusinessException;
import pe.inpe.ms_traslado.exception.ResourceNotFoundException;
import pe.inpe.ms_traslado.mapper.TrasladoMapper;
import pe.inpe.ms_traslado.remote.MsInstitucionesClient;
import pe.inpe.ms_traslado.remote.MsInternoClient;
import pe.inpe.ms_traslado.repository.TrasladoCustodiaRepository;
import pe.inpe.ms_traslado.repository.TrasladoRepository;
import pe.inpe.ms_traslado.service.TrasladoService;

import java.time.LocalDateTime;
import java.util.List;

import static pe.inpe.ms_traslado.util.GeneralConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrasladoServiceImpl implements TrasladoService {

    private final TrasladoRepository trasladoRepository;
    private final TrasladoMapper trasladoMapper;
    private final MsInternoClient internoClient;
    private final MsInstitucionesClient institucionesClient;

    @Override
    public TrasladoResponseDTO registrar(TrasladoRequestDTO dto) {
        log.info("Registrando traslado para interno id: {}", dto.getIdInterno());

        validarInternoExiste(dto.getIdInterno());
        validarInternoSinTrasladoActivo(dto.getIdInterno());
        validarSedeExiste(dto.getSedeOrigenId());
        validarSedeExiste(dto.getSedeDestinoId());
        validarSedesDistintas(dto.getSedeOrigenId(), dto.getSedeDestinoId());

        Traslado traslado = trasladoMapper.toEntity(dto);
        traslado.setEstadoTrasladoId(ESTADO_TRASLADO_PROGRAMADO);

        Traslado guardado = trasladoRepository.save(traslado);
        log.info("Traslado registrado con id: {}", guardado.getIdTraslado());
        return trasladoMapper.toDto(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrasladoResponseDTO> listarConFiltros(Long estado, Long sedeOrigen,
                                                      Long sedeDestino,
                                                      LocalDateTime fechaInicio,
                                                      LocalDateTime fechaFin) {
        log.info("Listando traslados con filtros - estado: {}, sedeOrigen: {}, sedeDestino: {}",
                estado, sedeOrigen, sedeDestino);
        return trasladoMapper.toDtoList(
                trasladoRepository.findByFiltros(estado, sedeOrigen, sedeDestino, fechaInicio, fechaFin)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TrasladoResponseDTO obtenerPorId(Long id) {
        log.info("Obteniendo traslado id: {}", id);
        return trasladoMapper.toDto(buscarPorId(id));
    }

    @Override
    public TrasladoResponseDTO actualizar(Long id, TrasladoRequestDTO dto) {
        log.info("Actualizando traslado id: {}", id);

        Traslado traslado = buscarPorId(id);
        validarEstadoModificable(traslado);
        validarSedeExiste(dto.getSedeOrigenId());
        validarSedeExiste(dto.getSedeDestinoId());
        validarSedesDistintas(dto.getSedeOrigenId(), dto.getSedeDestinoId());

        trasladoMapper.updateEntityFromDto(dto, traslado);
        return trasladoMapper.toDto(trasladoRepository.save(traslado));
    }

    @Override
    public TrasladoResponseDTO cambiarEstado(Long id, EstadoUpdateDTO dto) {
        log.info("Cambiando estado del traslado id: {} → estado: {}", id, dto.getEstadoTrasladoId());

        Traslado traslado = buscarPorId(id);
        validarTransicionEstado(traslado.getEstadoTrasladoId(), dto.getEstadoTrasladoId());

        traslado.setEstadoTrasladoId(dto.getEstadoTrasladoId());
        if (dto.getObservaciones() != null) {
            traslado.setObservaciones(dto.getObservaciones());
        }
        return trasladoMapper.toDto(trasladoRepository.save(traslado));
    }

    @Override
    public TrasladoResponseDTO registrarLlegada(Long id, LlegadaRegistroDTO dto) {
        log.info("Registrando llegada del traslado id: {}", id);

        Traslado traslado = buscarPorId(id);

        if (!traslado.getEstadoTrasladoId().equals(ESTADO_TRASLADO_EN_TRANSITO)) {
            throw new BusinessException("Solo se puede registrar llegada de traslados en estado EN_TRANSITO");
        }

        traslado.setFechaLlegada(dto.getFechaLlegada());
        traslado.setEstadoTrasladoId(ESTADO_TRASLADO_COMPLETADO);
        return trasladoMapper.toDto(trasladoRepository.save(traslado));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrasladoResponseDTO> listarPorInterno(Long idInterno) {
        log.info("Listando traslados del interno id: {}", idInterno);
        return trasladoMapper.toDtoList(
                trasladoRepository.findByIdInternoOrderByFechaTrasladoDesc(idInterno)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrasladoResponseDTO> listarPorSedeOrigen(Long idSede) {
        log.info("Listando traslados con sede origen id: {}", idSede);
        return trasladoMapper.toDtoList(trasladoRepository.findBySedeOrigenId(idSede));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrasladoResponseDTO> listarPorSedeDestino(Long idSede) {
        log.info("Listando traslados con sede destino id: {}", idSede);
        return trasladoMapper.toDtoList(trasladoRepository.findBySedeDestinoId(idSede));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrasladoResponseDTO> listarEnCurso() {
        log.info("Listando traslados en curso");
        return trasladoMapper.toDtoList(
                trasladoRepository.findByEstadoTrasladoIdIn(List.of(ESTADO_TRASLADO_EN_TRANSITO))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ValidacionTrasladoActivoDTO validarTrasladosActivos(Long idInterno) {
        log.info("Validando traslados activos del interno id: {}", idInterno);

        boolean tieneActivo = trasladoRepository
                .existsByIdInternoAndEstadoTrasladoIdIn(idInterno, ESTADOS_TRASLADO_ACTIVOS);

        if (!tieneActivo) {
            return ValidacionTrasladoActivoDTO.builder()
                    .idInterno(idInterno)
                    .tieneTrasladoActivo(false)
                    .idTrasladoActivo(null)
                    .estadoActual(null)
                    .build();
        }

        // Trae el más reciente para exponer su id y estado
        Traslado activo = trasladoRepository
                .findByIdInternoOrderByFechaTrasladoDesc(idInterno)
                .stream()
                .filter(t -> ESTADOS_TRASLADO_ACTIVOS.contains(t.getEstadoTrasladoId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Traslado activo no encontrado"));

        return ValidacionTrasladoActivoDTO.builder()
                .idInterno(idInterno)
                .tieneTrasladoActivo(true)
                .idTrasladoActivo(activo.getIdTraslado())
                .estadoActual(activo.getEstadoTrasladoId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ValidacionCapacidadDTO validarCapacidadSede(Long idSede) {
        log.info("Validando capacidad de sede id: {}", idSede);

        GenericResponseDTO<Boolean> disponibilidadResponse = institucionesClient.verificarDisponibilidad(idSede);

        if (disponibilidadResponse == null || disponibilidadResponse.getError() != null) {
            throw new ResourceNotFoundException("No se pudo obtener disponibilidad de la sede id: " + idSede);
        }

        Boolean disponible = disponibilidadResponse.getResponse();

        GenericResponseDTO<CapacidadSedeDTO> capacidadResponse = institucionesClient.obtenerCapacidadSede(idSede);
        CapacidadSedeDTO capacidad = capacidadResponse != null ? capacidadResponse.getResponse() : null;

        int capacidadMaxima = capacidad != null ? capacidad.getCapacidadMaxima() : 0;
        int ocupacionActual = capacidad != null ? capacidad.getOcupacionActual() : 0;
        int disponibles = capacidadMaxima - ocupacionActual;

        return ValidacionCapacidadDTO.builder()
                .idSede(idSede)
                .puedeRecibirInternos(Boolean.TRUE.equals(disponible))
                .capacidadMaxima(capacidadMaxima)
                .ocupacionActual(ocupacionActual)
                .disponibles(disponibles)
                .build();
    }

    private Traslado buscarPorId(Long id) {
        return trasladoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Traslado no encontrado con id: " + id));
    }

    private void validarInternoExiste(Long idInterno) {
        try {
            GenericResponseDTO<InternoResponseDTO> response = internoClient.obtenerInterno(idInterno);
            if (response == null || response.getError() != null || response.getResponse() == null) {
                throw new ResourceNotFoundException("Interno no encontrado con id: " + idInterno);
            }
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Interno no encontrado con id: " + idInterno);
        }
    }

    private void validarSedeExiste(Long idSede) {
        try {
            GenericResponseDTO<SedeResponseDTO> response = institucionesClient.obtenerSede(idSede);
            if (response == null || response.getError() != null || response.getResponse() == null) {
                throw new ResourceNotFoundException("Sede no encontrada con id: " + idSede);
            }
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Sede no encontrada con id: " + idSede);
        }
    }

    private void validarSedesDistintas(Long sedeOrigenId, Long sedeDestinoId) {
        if (sedeOrigenId.equals(sedeDestinoId)) {
            throw new BusinessException("La sede origen y la sede destino no pueden ser la misma");
        }
    }

    private void validarInternoSinTrasladoActivo(Long idInterno) {
        boolean tieneActivo = trasladoRepository
                .existsByIdInternoAndEstadoTrasladoIdIn(idInterno, ESTADOS_TRASLADO_ACTIVOS);
        if (tieneActivo) {
            throw new BusinessException("El interno con id " + idInterno + " ya tiene un traslado activo");
        }
    }

    private void validarEstadoModificable(Traslado traslado) {
        if (!traslado.getEstadoTrasladoId().equals(ESTADO_TRASLADO_PROGRAMADO)) {
            throw new BusinessException("Solo se pueden modificar traslados en estado PROGRAMADO");
        }
    }

    private void validarTransicionEstado(Long estadoActual, Long estadoNuevo) {
        boolean valido = switch (estadoActual.intValue()) {
            case 1 -> estadoNuevo.equals(ESTADO_TRASLADO_EN_TRANSITO)
                    || estadoNuevo.equals(ESTADO_TRASLADO_CANCELADO);
            case 2 -> estadoNuevo.equals(ESTADO_TRASLADO_COMPLETADO)
                    || estadoNuevo.equals(ESTADO_TRASLADO_CANCELADO);
            default -> false;
        };

        if (!valido) {
            throw new BusinessException(
                    "Transición de estado no permitida: " + estadoActual + " → " + estadoNuevo);
        }
    }
}
