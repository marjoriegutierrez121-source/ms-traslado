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
import pe.inpe.ms_traslado.mapper.TrasladoCustodiaMapper;
import pe.inpe.ms_traslado.mapper.TrasladoMapper;
import pe.inpe.ms_traslado.mapper.client.IInstitucionClientMapper;
import pe.inpe.ms_traslado.mapper.client.IInternoClientMapper;
import pe.inpe.ms_traslado.mapper.client.IJudicialClientMapper;
import pe.inpe.ms_traslado.remote.MsInstitucionesClient;
import pe.inpe.ms_traslado.remote.MsInternoClient;
import pe.inpe.ms_traslado.remote.MsJudicialClient;
import pe.inpe.ms_traslado.repository.TrasladoCustodiaRepository;
import pe.inpe.ms_traslado.repository.TrasladoRepository;
import pe.inpe.ms_traslado.service.IOrquestacionTrasladoService;
import pe.inpe.ms_traslado.service.TrasladoCustodiaService;
import pe.inpe.ms_traslado.service.TrasladoService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static pe.inpe.ms_traslado.util.GeneralConstants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrquestacionTrasladoServiceImpl implements IOrquestacionTrasladoService {

    private final TrasladoCustodiaService custodiaService;
    private final TrasladoService trasladoService;
    private final TrasladoRepository trasladoRepository;
    private final TrasladoCustodiaRepository custodiaRepository;
    private final TrasladoMapper trasladoMapper;
    private final TrasladoCustodiaMapper custodiaMapper;

    private final MsInternoClient internoClient;
    private final MsInstitucionesClient institucionesClient;
    private final MsJudicialClient judicialClient;

    private final IInternoClientMapper internoClientMapper;
    private final IInstitucionClientMapper institucionClientMapper;
    private final IJudicialClientMapper judicialClientMapper;

    @Override
    @Transactional
    public SolicitudTrasladoResponseDTO solicitarTraslado(SolicitudTrasladoRequestDTO request) {
        log.info("Iniciando solicitud de traslado para interno: {}", request.getIdInterno());

        // 1. Validar que el interno existe y puede ser trasladado
        ValidacionInternoDTO validacionInterno = validarInternoParaTraslado(request.getIdInterno());
        if (!validacionInterno.getPuedeSerTrasladado()) {
            throw new BusinessException("El interno no puede ser trasladado: " + validacionInterno.getMotivo());
        }

        // 2. Validar que no tenga un traslado activo
        if (tieneTrasladoActivo(request.getIdInterno())) {
            throw new BusinessException("El interno ya tiene un traslado activo (programado o en tránsito)");
        }

        // 3. Obtener ubicación actual como sede origen
        Long sedeOrigenId = obtenerSedeOrigenActual(request.getIdInterno());
        if (sedeOrigenId == null) {
            throw new BusinessException("No se pudo determinar la ubicación actual del interno");
        }

        // 4. Validar sede destino
        validarSedeDestino(request.getSedeDestinoId());

        // 5. Validar que la sede destino sea diferente a la sede actual
        if (sedeOrigenId.equals(request.getSedeDestinoId())) {
            throw new BusinessException("La sede de origen y la sede de destino no pueden ser la misma");
        }

        // 6. Validar restricciones judiciales
        validarRestriccionesJudiciales(request.getIdInterno());

        // 7. Crear el traslado
        TrasladoRequestDTO trasladoRequest = TrasladoRequestDTO.builder()
                .idInterno(request.getIdInterno())
                .sedeOrigenId(sedeOrigenId)
                .sedeDestinoId(request.getSedeDestinoId())
                .causaId(request.getCausaId())
                .idResolucion(request.getIdResolucion())
                .fechaTraslado(request.getFechaTraslado())
                .observaciones(request.getObservaciones())
                .build();

        TrasladoResponseDTO trasladoCreado = trasladoService.registrar(trasladoRequest);
        log.info("Traslado creado con ID: {}", trasladoCreado.getIdTraslado());

        // 8. Asignar custodios
        if (request.getCustodios() != null && !request.getCustodios().isEmpty()) {
            for (var custodiaRequest : request.getCustodios()) {
                custodiaService.asignarCustodia(trasladoCreado.getIdTraslado(), custodiaRequest);
            }
            log.info("Asignados {} custodios al traslado {}", request.getCustodios().size(), trasladoCreado.getIdTraslado());
        }

        return SolicitudTrasladoResponseDTO.builder()
                .idTraslado(trasladoCreado.getIdTraslado())
                .estadoTrasladoId(trasladoCreado.getEstadoTrasladoId())
                .fechaTraslado(trasladoCreado.getFechaTraslado())
                .sedeOrigenId(sedeOrigenId)
                .sedeDestinoId(trasladoCreado.getSedeDestinoId())
                .mensaje("Traslado solicitado exitosamente")
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public TrasladoDetalleResponseDTO obtenerFichaCompletaTraslado(Long idTraslado) {
        log.info("Obteniendo ficha completa del traslado: {}", idTraslado);

        TrasladoResponseDTO traslado = trasladoService.obtenerPorId(idTraslado);

        TrasladoDetalleResponseDTO detalle = TrasladoDetalleResponseDTO.builder()
                .traslado(traslado)
                .build();

        try {
            GenericResponseDTO<InternoResponseDTO> internoResp = internoClient.obtenerInterno(traslado.getIdInterno());
            if (internoResp != null && internoResp.getResponse() != null) {
                InternoResponseDTO interno = internoResp.getResponse();
                detalle.setInterno(internoClientMapper.toInternoResumenDTO(interno));

                GenericResponseDTO<InternoUbicacionResponseDTO> ubicacionResp = internoClient.obtenerUbicacionActual(traslado.getIdInterno());
                if (ubicacionResp != null && ubicacionResp.getResponse() != null) {
                    detalle.setUbicacionActual(internoClientMapper.toUbicacionResumenDTO(ubicacionResp.getResponse()));
                }

                GenericResponseDTO<ClasificacionInternoResponseDTO> clasificacionResp = internoClient.obtenerClasificacionActual(traslado.getIdInterno());
                if (clasificacionResp != null && clasificacionResp.getResponse() != null) {
                    detalle.setClasificacionActual(internoClientMapper.toClasificacionResumenDTO(clasificacionResp.getResponse()));
                }
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener datos del interno: {}", e.getMessage());
        }

        try {
            GenericResponseDTO<SedeResponseDTO> sedeOrigenResp = institucionesClient.obtenerSede(traslado.getSedeOrigenId());
            if (sedeOrigenResp != null && sedeOrigenResp.getResponse() != null) {
                detalle.setSedeOrigen(institucionClientMapper.toSedeResumenDTO(sedeOrigenResp.getResponse()));
            }

            GenericResponseDTO<SedeResponseDTO> sedeDestinoResp = institucionesClient.obtenerSede(traslado.getSedeDestinoId());
            if (sedeDestinoResp != null && sedeDestinoResp.getResponse() != null) {
                detalle.setSedeDestino(institucionClientMapper.toSedeResumenDTO(sedeDestinoResp.getResponse()));
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener datos de sedes: {}", e.getMessage());
        }

        detalle.setCustodios(custodiaService.listarPorTraslado(idTraslado));

        ResumenJudicialDTO resumenJudicial = obtenerResumenJudicial(traslado.getIdInterno());
        detalle.setResumenJudicial(resumenJudicial);

        boolean puedeTrasladar = puedeSerTrasladado(traslado.getIdInterno());
        detalle.setPuedeSerTrasladado(puedeTrasladar);
        if (!puedeTrasladar) {
            detalle.setMotivoRestriccion(obtenerMotivoRestriccionTraslado(traslado.getIdInterno()));
        }

        detalle.setFechaConsulta(LocalDateTime.now());

        return detalle;
    }

    @Override
    @Transactional(readOnly = true)
    public ValidacionPreviaResponseDTO validarPreTraslado(ValidacionPreviaRequestDTO request) {
        log.info("Realizando validación previa de traslado para interno: {}", request.getIdInterno());

        List<ValidacionItemDTO> validaciones = new ArrayList<>();
        boolean puedeSerTrasladado = true;
        Long sedeActualId = null;

        try {
            ValidacionInternoDTO validacionInterno = validarInternoParaTraslado(request.getIdInterno());
            if (!validacionInterno.getPuedeSerTrasladado()) {
                validaciones.add(ValidacionItemDTO.builder()
                        .campo("interno")
                        .valida(false)
                        .mensaje(validacionInterno.getMotivo())
                        .nivel("ERROR")
                        .build());
                puedeSerTrasladado = false;
            } else {
                validaciones.add(ValidacionItemDTO.builder()
                        .campo("interno")
                        .valida(true)
                        .mensaje("Interno válido para traslado")
                        .nivel("INFO")
                        .build());
            }
        } catch (Exception e) {
            validaciones.add(ValidacionItemDTO.builder()
                    .campo("interno")
                    .valida(false)
                    .mensaje("Error validando interno: " + e.getMessage())
                    .nivel("ERROR")
                    .build());
            puedeSerTrasladado = false;
        }

        try {
            sedeActualId = obtenerSedeOrigenActual(request.getIdInterno());
            if (sedeActualId != null) {
                validaciones.add(ValidacionItemDTO.builder()
                        .campo("sedeActual")
                        .valida(true)
                        .mensaje("Sede actual del interno: " + sedeActualId)
                        .nivel("INFO")
                        .build());
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener sede actual: {}", e.getMessage());
        }

        try {
            boolean tieneActivo = tieneTrasladoActivo(request.getIdInterno());
            if (tieneActivo) {
                validaciones.add(ValidacionItemDTO.builder()
                        .campo("trasladoActivo")
                        .valida(false)
                        .mensaje("El interno ya tiene un traslado activo")
                        .nivel("ERROR")
                        .build());
                puedeSerTrasladado = false;
            } else {
                validaciones.add(ValidacionItemDTO.builder()
                        .campo("trasladoActivo")
                        .valida(true)
                        .mensaje("No tiene traslados activos")
                        .nivel("INFO")
                        .build());
            }
        } catch (Exception e) {
            log.warn("Error validando traslados activos: {}", e.getMessage());
        }

        ValidacionCapacidadDTO capacidad = null;
        if (request.getIdSedeDestino() != null) {
            try {
                if (sedeActualId != null && sedeActualId.equals(request.getIdSedeDestino())) {
                    validaciones.add(ValidacionItemDTO.builder()
                            .campo("sedeDestino")
                            .valida(false)
                            .mensaje("La sede destino no puede ser la misma que la sede actual del interno")
                            .nivel("ERROR")
                            .build());
                    puedeSerTrasladado = false;
                } else {
                    capacidad = trasladoService.validarCapacidadSede(request.getIdSedeDestino());
                    if (!capacidad.getPuedeRecibirInternos()) {
                        validaciones.add(ValidacionItemDTO.builder()
                                .campo("sedeDestino")
                                .valida(false)
                                .mensaje("La sede destino no tiene capacidad disponible")
                                .nivel("ERROR")
                                .build());
                        puedeSerTrasladado = false;
                    } else {
                        validaciones.add(ValidacionItemDTO.builder()
                                .campo("sedeDestino")
                                .valida(true)
                                .mensaje("Sede destino disponible (" + capacidad.getDisponibles() + " cupos)")
                                .nivel("INFO")
                                .build());
                    }
                }
            } catch (Exception e) {
                validaciones.add(ValidacionItemDTO.builder()
                        .campo("sedeDestino")
                        .valida(false)
                        .mensaje("Error validando sede destino: " + e.getMessage())
                        .nivel("ERROR")
                        .build());
                puedeSerTrasladado = false;
            }
        }

        // Validar restricciones judiciales
        ResumenJudicialDTO resumenJudicial = null;
        try {
            resumenJudicial = obtenerResumenJudicial(request.getIdInterno());
            if (resumenJudicial.getTieneRestricciones() != null && resumenJudicial.getTieneRestricciones()) {
                validaciones.add(ValidacionItemDTO.builder()
                        .campo("judicial")
                        .valida(false)
                        .mensaje("Restricciones judiciales: " + String.join(", ", resumenJudicial.getRestricciones()))
                        .nivel("ERROR")
                        .build());
                puedeSerTrasladado = false;
            } else {
                validaciones.add(ValidacionItemDTO.builder()
                        .campo("judicial")
                        .valida(true)
                        .mensaje("Sin restricciones judiciales")
                        .nivel("INFO")
                        .build());
            }
        } catch (Exception e) {
            log.warn("No se pudo validar restricciones judiciales: {}", e.getMessage());
        }

        return ValidacionPreviaResponseDTO.builder()
                .idInterno(request.getIdInterno())
                .puedeSerTrasladado(puedeSerTrasladado)
                .validaciones(validaciones)
                .restriccionesJudiciales(resumenJudicial)
                .capacidadSede(capacidad)
                .sedeActualId(sedeActualId)
                .build();
    }

    @Override
    @Transactional
    public void iniciarTraslado(Long idTraslado, String usuario) {
        log.info("Iniciando traslado {} por usuario {}", idTraslado, usuario);

        TrasladoResponseDTO traslado = trasladoService.obtenerPorId(idTraslado);

        if (!ESTADO_TRASLADO_PROGRAMADO.equals(traslado.getEstadoTrasladoId())) {
            throw new BusinessException("Solo se puede iniciar un traslado en estado PROGRAMADO. Estado actual: " + traslado.getEstadoTrasladoId());
        }

        EstadoUpdateDTO estadoUpdate = EstadoUpdateDTO.builder()
                .estadoTrasladoId(ESTADO_TRASLADO_EN_TRANSITO)
                .observaciones("Traslado iniciado por: " + usuario)
                .build();

        trasladoService.cambiarEstado(idTraslado, estadoUpdate);
        log.info("Traslado {} cambiado a estado EN_TRANSITO", idTraslado);
    }

    @Override
    @Transactional
    public void completarTraslado(Long idTraslado, LocalDateTime fechaLlegada, String usuario, String observaciones) {
        log.info("Completando traslado {} por usuario {}", idTraslado, usuario);

        TrasladoResponseDTO traslado = trasladoService.obtenerPorId(idTraslado);

        if (!ESTADO_TRASLADO_EN_TRANSITO.equals(traslado.getEstadoTrasladoId())) {
            throw new BusinessException("Solo se puede completar un traslado en estado EN_TRANSITO. Estado actual: " + traslado.getEstadoTrasladoId());
        }

        LlegadaRegistroDTO llegadaDTO = LlegadaRegistroDTO.builder()
                .fechaLlegada(fechaLlegada != null ? fechaLlegada : LocalDateTime.now())
                .build();
        trasladoService.registrarLlegada(idTraslado, llegadaDTO);

        log.info("Traslado {} completado", idTraslado);
    }

    @Override
    @Transactional
    public void cancelarTraslado(Long idTraslado, String motivo, String usuario) {
        log.info("Cancelando traslado {} por usuario {}. Motivo: {}", idTraslado, usuario, motivo);

        TrasladoResponseDTO traslado = trasladoService.obtenerPorId(idTraslado);

        if (ESTADO_TRASLADO_COMPLETADO.equals(traslado.getEstadoTrasladoId())) {
            throw new BusinessException("No se puede cancelar un traslado ya completado");
        }

        EstadoUpdateDTO estadoUpdate = EstadoUpdateDTO.builder()
                .estadoTrasladoId(ESTADO_TRASLADO_CANCELADO)
                .observaciones("Cancelado por: " + usuario + ". Motivo: " + motivo)
                .build();

        trasladoService.cambiarEstado(idTraslado, estadoUpdate);
        log.info("Traslado {} cancelado", idTraslado);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneTrasladoActivo(Long idInterno) {
        ValidacionTrasladoActivoDTO validacion = trasladoService.validarTrasladosActivos(idInterno);
        return validacion.getTieneTrasladoActivo();
    }

    @Override
    @Transactional(readOnly = true)
    public TrasladoResponseDTO obtenerTrasladoActivo(Long idInterno) {
        ValidacionTrasladoActivoDTO validacion = trasladoService.validarTrasladosActivos(idInterno);
        if (validacion.getTieneTrasladoActivo() && validacion.getIdTrasladoActivo() != null) {
            return trasladoService.obtenerPorId(validacion.getIdTrasladoActivo());
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrasladoResponseDTO> listarTrasladosPorInterno(Long idInterno) {
        return trasladoService.listarPorInterno(idInterno);
    }

    /**
     * Valida si un interno existe y puede ser trasladado
     */
    private ValidacionInternoDTO validarInternoParaTraslado(Long idInterno) {
        try {
            GenericResponseDTO<Boolean> puedeResp = internoClient.puedeSerTrasladado(idInterno);
            if (puedeResp != null && Boolean.TRUE.equals(puedeResp.getResponse())) {
                return ValidacionInternoDTO.builder()
                        .puedeSerTrasladado(true)
                        .build();
            }

            String motivo = null;
            try {
                GenericResponseDTO<String> motivoResp = internoClient.obtenerMotivoRestriccionTraslado(idInterno);
                if (motivoResp != null) {
                    motivo = motivoResp.getResponse();
                }
            } catch (Exception e) {
                motivo = "Restricción no especificada por ms-interno";
            }

            return ValidacionInternoDTO.builder()
                    .puedeSerTrasladado(false)
                    .motivo(motivo)
                    .build();
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Interno no encontrado con ID: " + idInterno);
        }
    }

    /**
     * Obtiene la sede actual (origen) del interno desde ms-interno
     */
    private Long obtenerSedeOrigenActual(Long idInterno) {
        try {
            GenericResponseDTO<InternoUbicacionResponseDTO> ubicacionResp = internoClient.obtenerUbicacionActual(idInterno);
            if (ubicacionResp != null && ubicacionResp.getResponse() != null) {
                return ubicacionResp.getResponse().getIdInstitutoSede();
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener ubicación actual del interno: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Valida que la sede destino exista, esté activa y tenga capacidad
     */
    private void validarSedeDestino(Long idSede) {
        try {
            GenericResponseDTO<SedeResponseDTO> sedeResp = institucionesClient.obtenerSede(idSede);
            if (sedeResp == null || sedeResp.getError() != null || sedeResp.getResponse() == null) {
                throw new BusinessException("Sede destino no encontrada");
            }

            SedeResponseDTO sede = sedeResp.getResponse();
            if (!Boolean.TRUE.equals(sede.getActivo())) {
                throw new BusinessException("La sede destino no está activa");
            }

            ValidacionCapacidadDTO capacidad = trasladoService.validarCapacidadSede(idSede);
            if (!capacidad.getPuedeRecibirInternos()) {
                throw new BusinessException("La sede destino no tiene cupos disponibles");
            }
        } catch (FeignException.NotFound e) {
            throw new BusinessException("Sede destino no encontrada");
        }
    }

    /**
     * Valida restricciones judiciales del interno
     */
    private void validarRestriccionesJudiciales(Long idInterno) {
        try {
            GenericResponseDTO<RestriccionesTrasladoDTO> restriccionesResp = judicialClient.obtenerRestriccionesTraslado(idInterno);
            if (restriccionesResp != null && restriccionesResp.getResponse() != null) {
                RestriccionesTrasladoDTO restricciones = restriccionesResp.getResponse();
                if (!Boolean.TRUE.equals(restricciones.getPuedeSerTrasladado())) {
                    String mensaje = restricciones.getRestricciones() != null ?
                            String.join(", ", restricciones.getRestricciones()) :
                            "Restricciones judiciales impiden el traslado";
                    throw new BusinessException("Restricciones judiciales: " + mensaje);
                }
            }
        } catch (FeignException e) {
            log.warn("Error consultando restricciones judiciales: {}", e.getMessage());
        }
    }

    /**
     * Obtiene resumen judicial del interno (expedientes y mandato)
     */
    private ResumenJudicialDTO obtenerResumenJudicial(Long idInterno) {
        try {
            GenericResponseDTO<List<ExpedienteResponseDTO>> expedientesResp = judicialClient.listarExpedientesPorInterno(idInterno);
            List<ExpedienteResponseDTO> expedientes = expedientesResp != null ? expedientesResp.getResponse() : null;

            MandatoActivoDTO mandato = null;
            try {
                GenericResponseDTO<MandatoActivoDTO> mandatoResp = judicialClient.obtenerMandatoActivo(idInterno);
                if (mandatoResp != null) {
                    mandato = mandatoResp.getResponse();
                }
            } catch (Exception e) {
                log.debug("Interno {} no tiene mandato activo", idInterno);
            }

            return judicialClientMapper.toResumenJudicial(expedientes, mandato);
        } catch (Exception e) {
            log.warn("No se pudo obtener resumen judicial: {}", e.getMessage());
            return judicialClientMapper.emptyResumenJudicial();
        }
    }

    /**
     * Verifica si un interno puede ser trasladado (versión simple)
     */
    private boolean puedeSerTrasladado(Long idInterno) {
        try {
            GenericResponseDTO<Boolean> response = internoClient.puedeSerTrasladado(idInterno);
            return response != null && Boolean.TRUE.equals(response.getResponse());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene el motivo por el cual un interno no puede ser trasladado
     */
    private String obtenerMotivoRestriccionTraslado(Long idInterno) {
        try {
            GenericResponseDTO<String> response = internoClient.obtenerMotivoRestriccionTraslado(idInterno);
            return response != null ? response.getResponse() : "Restricción no especificada";
        } catch (Exception e) {
            return "No se pudo obtener el motivo de restricción";
        }
    }
}
