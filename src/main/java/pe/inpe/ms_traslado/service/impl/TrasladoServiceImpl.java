package pe.inpe.ms_traslado.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.inpe.ms_traslado.dto.*;
import pe.inpe.ms_traslado.entity.Traslado;
import pe.inpe.ms_traslado.entity.TrasladoCustodia;
import pe.inpe.ms_traslado.exception.BadRequestException;
import pe.inpe.ms_traslado.exception.ResourceNotFoundException;
import pe.inpe.ms_traslado.mapper.TrasladoCustodiaMapper;
import pe.inpe.ms_traslado.mapper.TrasladoMapper;
import pe.inpe.ms_traslado.repository.TrasladoCustodiaRepository;
import pe.inpe.ms_traslado.repository.TrasladoRepository;
import pe.inpe.ms_traslado.service.TrasladoService;
import pe.inpe.ms_traslado.util.TrasladoEstados;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrasladoServiceImpl implements TrasladoService {

    public final TrasladoRepository trasladoRepository;
    public final TrasladoCustodiaRepository trasladoCustodiaRepository;
    public final TrasladoMapper trasladoMapper;


    @Override
    @Transactional
    public TrasladoResponseDTO addTraslado(TrasladoRequestDTO trasladoDto) {
        log.info("Registrando nuevo traslado para interno: {}", trasladoDto.getIdInterno());
        if (trasladoRepository.existsByIdInternoAndEstadoTrasladoIdIn(
                trasladoDto.getIdInterno(), TrasladoEstados.ESTADOS_ACTIVOS)) {
            throw new IllegalStateException("El interno ya tiene un traslado activo (Programado o En tránsito)");
        }

        if (trasladoDto.getSedeOrigenId().equals(trasladoDto.getSedeDestinoId())) {
            throw new IllegalArgumentException("La sede origen y destino no pueden ser iguales");
        }
        if (trasladoDto.getFechaTraslado().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de traslado no puede ser en el pasado");
        }
        Traslado traslado = trasladoMapper.toEntity(trasladoDto);
        if (traslado.getEstadoTrasladoId() == null) {
            traslado.setEstadoTrasladoId(TrasladoEstados.PROGRAMADO);
        }
        traslado.setRegistrationDate(LocalDateTime.now());
        traslado.setRegistrationUser("SYSTEM");
        traslado.setLastModificationDate(LocalDateTime.now());
        traslado.setLastModificationUser("SYSTEM");

        Traslado saved = trasladoRepository.save(traslado);
        log.info("Traslado registrado exitosamente con ID: {}", saved.getIdTraslado());
        return trasladoMapper.toResponseDTO(saved);
    }

    @Override
    public List<TrasladoResponseDTO> getTrasladosConFiltros(Long estado, Long sedeOrigen, Long sedeDestino, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Listando traslados con filtros - estado: {}, sedeOrigen: {}, sedeDestino: {}",
                estado, sedeOrigen, sedeDestino);
        List<Traslado> traslados= trasladoRepository.findByFiltros(
                estado, sedeOrigen, sedeDestino, fechaInicio, fechaFin);
        return trasladoMapper.toResponseDTOList(traslados);
    }

    @Override
    public TrasladoResponseDTO getTrasladoXId(Long id) {
        log.info("Buscando traslado con ID :"+id);
        Traslado traslado= trasladoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Traslado no encontrado con ID :"+id));
        TrasladoResponseDTO response= trasladoMapper.toResponseDTO(traslado);
        return response;
    }

    @Override
    public List<TrasladoResponseDTO> getTrasladosXIdInterno(Long idInterno) {
        log.info("Listando traslados del interno: {}", idInterno);
        List<Traslado> traslados = trasladoRepository.findByIdInterno(idInterno);
        return trasladoMapper.toResponseDTOList(traslados);
    }

    @Override
    @Transactional
    public TrasladoResponseDTO putTraslado(Long id, TrasladoRequestDTO trasladoDto) {
        log.info("Actualizando traslado ID: {}", id);

        Traslado traslado = trasladoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Traslado no encontrado con ID: " + id));

        if (!TrasladoEstados.PROGRAMADO.equals(traslado.getEstadoTrasladoId())) {
            throw new IllegalStateException("Solo se pueden modificar traslados en estado PROGRAMADO");
        }

        if (trasladoDto.getFechaTraslado().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de traslado no puede ser en el pasado");
        }

        trasladoMapper.updateEntity(trasladoDto, traslado);
        traslado.setLastModificationDate(LocalDateTime.now());
        traslado.setLastModificationUser("SYSTEM");

        Traslado updated = trasladoRepository.save(traslado);
        return trasladoMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public TrasladoResponseDTO patchCambiarEstado(Long id, CambioEstadoDTO cambioEstadoDto) {
        log.info("Cambiando estado del traslado ID: {} a estado: {}", id, cambioEstadoDto.getEstadoTrasladoId());

        Traslado traslado = trasladoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Traslado no encontrado con ID: " + id));

        Long estadoActual = traslado.getEstadoTrasladoId();
        Long nuevoEstado = cambioEstadoDto.getEstadoTrasladoId();

        if (!TrasladoEstados.puedeCambiarA(estadoActual, nuevoEstado)) {
            throw new IllegalStateException(
                    String.format("No se puede cambiar de estado %d a %d", estadoActual, nuevoEstado));
        }

        if (TrasladoEstados.EN_TRANSITO.equals(nuevoEstado)) {
            List<TrasladoCustodia> custodias = trasladoCustodiaRepository.findByTrasladoIdTraslado(id);
            if (custodias.isEmpty()) {
                throw new IllegalStateException("No se puede iniciar el traslado sin custodios asignados");
            }
        }

        if (TrasladoEstados.COMPLETADO.equals(nuevoEstado)) {
            if (traslado.getFechaLlegada() == null) {
                throw new IllegalStateException("No se puede completar un traslado sin registrar la llegada");
            }
        }

        traslado.setEstadoTrasladoId(nuevoEstado);
        if (cambioEstadoDto.getObservaciones() != null) {
            traslado.setObservaciones(cambioEstadoDto.getObservaciones());
        }
        traslado.setLastModificationDate(LocalDateTime.now());
        traslado.setLastModificationUser("SYSTEM");

        Traslado updated = trasladoRepository.save(traslado);
        return trasladoMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public TrasladoResponseDTO patchRegistrarLlegada(Long id, RegistroLlegadaDTO registroLlegadaDto) {
        log.info("Registrando llegada del traslado ID: {}", id);

        Traslado traslado = trasladoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Traslado no encontrado con ID: " + id));

        if (!TrasladoEstados.EN_TRANSITO.equals(traslado.getEstadoTrasladoId())) {
            throw new IllegalStateException("Solo se puede registrar llegada en traslados EN_TRANSITO");
        }

        if (registroLlegadaDto.getFechaLlegada().isBefore(traslado.getFechaTraslado())) {
            throw new IllegalArgumentException("La fecha de llegada no puede ser anterior a la fecha de salida");
        }

        traslado.setFechaLlegada(registroLlegadaDto.getFechaLlegada());
        if (registroLlegadaDto.getObservaciones() != null) {
            traslado.setObservaciones(registroLlegadaDto.getObservaciones());
        }
        traslado.setLastModificationDate(LocalDateTime.now());
        traslado.setLastModificationUser("SYSTEM");

        Traslado updated = trasladoRepository.save(traslado);
        return trasladoMapper.toResponseDTO(updated);
    }

    @Override
    public List<TrasladoResponseDTO> getTrasladosXSedeOrigen(Long idSede) {
        log.info("Listando traslados con sede origen: {}", idSede);

        List<Traslado> traslados = trasladoRepository.findBySedeOrigenId(idSede);
        return trasladoMapper.toResponseDTOList(traslados);
    }

    @Override
    public List<TrasladoResponseDTO> getTrasladosXSedeDestino(Long idSede) {
        log.info("Listando traslados con sede destino: {}", idSede);

        List<Traslado> traslados = trasladoRepository.findBySedeDestinoId(idSede);
        return trasladoMapper.toResponseDTOList(traslados);
    }

    @Override
    public List<TrasladoResponseDTO> getTrasladosEnCurso() {
        log.info("Listando traslados en curso (EN_TRANSITO)");

        List<Traslado> traslados = trasladoRepository.findByEstadoTrasladoId(TrasladoEstados.EN_TRANSITO);
        return trasladoMapper.toResponseDTOList(traslados);
    }

    @Override
    public ValidacionTrasladoDTO validarTrasladoActivo(Long idInterno) {
        log.info("Validando si el interno {} tiene traslado activo", idInterno);

        boolean tieneActivo = trasladoRepository.existsByIdInternoAndEstadoTrasladoIdIn(
                idInterno, TrasladoEstados.ESTADOS_ACTIVOS);

        ValidacionTrasladoDTO.ValidacionTrasladoDTOBuilder builder = ValidacionTrasladoDTO.builder()
                .tieneTrasladoActivo(tieneActivo);

        if (tieneActivo) {
            Traslado trasladoActivo = trasladoRepository.findTrasladoActivoByInterno(
                    idInterno, TrasladoEstados.ESTADOS_ACTIVOS).orElse(null);
            builder.trasladoActivo(trasladoMapper.toResponseDTO(trasladoActivo))
                    .mensaje("El interno tiene un traslado " +
                            TrasladoEstados.getNombre(trasladoActivo.getEstadoTrasladoId()));
        } else {
            builder.mensaje("El interno no tiene traslados activos");
        }

        return builder.build();
    }

    @Override
    public ValidacionCapacidadDTO verificarCapacidadSede(Long idSede) {
        log.info("Verificando capacidad de sede ID: {}", idSede);
        List<Traslado> trasladosActivos = trasladoRepository
                .findBySedeDestinoIdAndEstadoTrasladoIdIn(idSede, TrasladoEstados.ESTADOS_ACTIVOS);

        Integer internosActuales = trasladosActivos.size();

        return ValidacionCapacidadDTO.builder()
                .sedeId(idSede)
                .capacidadTotal(null)
                .internosActuales(null)
                .internosActuales(internosActuales)
                .disponibles(null)
                .puedeRecibir(null)
                .mensaje("Pendiente integración con ms-institucion y ms-personal")
                .build();
    }
}
