package pe.inpe.ms_traslado.service;

import pe.inpe.ms_traslado.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrquestacionTrasladoService {
    /**
     * Solicita un traslado completo.
     * Flujo:
     * 1. Valida que el interno existe y puede ser trasladado (ms-interno)
     * 2. Valida que la sede destino existe y tiene capacidad (ms-institucion)
     * 3. Valida restricciones judiciales (ms-judicial)
     * 4. Valida que no tenga traslado activo (ms-traslados)
     * 5. Valida que la sede destino sea DIFERENTE a la sede actual
     * 6. Crea el traslado con estado PROGRAMADO
     * 7. Asigna custodios (opcional)
     */
    SolicitudTrasladoResponseDTO solicitarTraslado(SolicitudTrasladoRequestDTO request);

    /**
     * Obtiene la ficha completa del traslado.
     * Agrega datos de:
     * - ms-interno (datos del interno, ubicación, clasificación)
     * - ms-institucion (datos de sedes origen y destino)
     * - ms-judicial (restricciones, mandato activo)
     * - ms-traslados (custodios asignados)
     */
    TrasladoDetalleResponseDTO obtenerFichaCompletaTraslado(Long idTraslado);

    /**
     * Valida previamente si un interno puede ser trasladado.
     * Retorna todas las validaciones sin crear el traslado.
     */
    ValidacionPreviaResponseDTO validarPreTraslado(ValidacionPreviaRequestDTO request);

    /**
     * Inicia el traslado (PROGRAMADO → EN_TRANSITO)
     */
    void iniciarTraslado(Long idTraslado, String usuario);

    /**
     * Completa el traslado (EN_TRANSITO → COMPLETADO)
     */
    void completarTraslado(Long idTraslado, LocalDateTime fechaLlegada, String usuario, String observaciones);

    /**
     * Cancela el traslado (→ CANCELADO)
     */
    void cancelarTraslado(Long idTraslado, String motivo, String usuario);

    /**
     * Valida si un interno tiene traslados activos
     */
    boolean tieneTrasladoActivo(Long idInterno);

    /**
     * Obtiene el traslado activo de un interno
     */
    TrasladoResponseDTO obtenerTrasladoActivo(Long idInterno);

    /**
     * Lista todos los traslados de un interno
     */
    List<TrasladoResponseDTO> listarTrasladosPorInterno(Long idInterno);
}
