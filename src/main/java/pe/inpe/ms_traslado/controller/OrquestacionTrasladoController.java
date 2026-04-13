package pe.inpe.ms_traslado.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.inpe.ms_traslado.dto.*;
import pe.inpe.ms_traslado.service.IOrquestacionTrasladoService;
import pe.inpe.ms_traslado.service.TrasladoService;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/traslados")
@RequiredArgsConstructor
public class OrquestacionTrasladoController {

    private final IOrquestacionTrasladoService orquestacionTrasladoService;

    @PostMapping("/solicitar")
    public ResponseEntity<GenericResponseDTO<SolicitudTrasladoResponseDTO>> solicitarTraslado(
            @Valid @RequestBody SolicitudTrasladoRequestDTO request) {
        log.info("POST /api/v1/traslados/orquestacion/solicitar - Interno: {}", request.getIdInterno());
        SolicitudTrasladoResponseDTO response = orquestacionTrasladoService.solicitarTraslado(request);
        return ResponseEntity.ok(GenericResponseDTO.<SolicitudTrasladoResponseDTO>builder().response(response).build());
    }

    @GetMapping("/{id}/ficha-completa")
    public ResponseEntity<GenericResponseDTO<TrasladoDetalleResponseDTO>> obtenerFichaCompletaTraslado(@PathVariable Long id) {
        log.info("GET /api/v1/traslados/orquestacion/{}/ficha-completa", id);
        TrasladoDetalleResponseDTO response = orquestacionTrasladoService.obtenerFichaCompletaTraslado(id);
        return ResponseEntity.ok(GenericResponseDTO.<TrasladoDetalleResponseDTO>builder().response(response).build());
    }

    @PostMapping("/validar-pre-traslado")
    public ResponseEntity<GenericResponseDTO<ValidacionPreviaResponseDTO>> validarPreTraslado(
            @Valid @RequestBody ValidacionPreviaRequestDTO request) {
        log.info("POST /api/v1/traslados/orquestacion/validar-pre-traslado - Interno: {}", request.getIdInterno());
        ValidacionPreviaResponseDTO response = orquestacionTrasladoService.validarPreTraslado(request);
        return ResponseEntity.ok(GenericResponseDTO.<ValidacionPreviaResponseDTO>builder().response(response).build());
    }

    @PutMapping("/{id}/iniciar")
    public ResponseEntity<GenericResponseDTO<Void>> iniciarTraslado(
            @PathVariable Long id,
            @RequestParam String usuario) {
        log.info("PUT /api/v1/traslados/orquestacion/{}/iniciar - Usuario: {}", id, usuario);
        orquestacionTrasladoService.iniciarTraslado(id, usuario);
        return ResponseEntity.ok(GenericResponseDTO.<Void>builder().response(null).build());
    }

    @PutMapping("/{id}/completar")
    public ResponseEntity<GenericResponseDTO<Void>> completarTraslado(
            @PathVariable Long id,
            @RequestParam String usuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaLlegada,
            @RequestParam(required = false) String observaciones) {
        log.info("PUT /api/v1/traslados/orquestacion/{}/completar - Usuario: {}", id, usuario);
        orquestacionTrasladoService.completarTraslado(id, fechaLlegada, usuario, observaciones);
        return ResponseEntity.ok(GenericResponseDTO.<Void>builder().response(null).build());
    }

    /**
     * Cancelar traslado
     */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<GenericResponseDTO<Void>> cancelarTraslado(
            @PathVariable Long id,
            @RequestParam String usuario,
            @RequestParam String motivo) {
        log.info("PUT /api/v1/traslados/orquestacion/{}/cancelar - Usuario: {}, Motivo: {}", id, usuario, motivo);
        orquestacionTrasladoService.cancelarTraslado(id, motivo, usuario);
        return ResponseEntity.ok(GenericResponseDTO.<Void>builder().response(null).build());
    }
}
