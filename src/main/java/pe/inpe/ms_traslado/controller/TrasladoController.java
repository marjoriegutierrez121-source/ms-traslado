package pe.inpe.ms_traslado.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.inpe.ms_traslado.dto.*;
import pe.inpe.ms_traslado.service.TrasladoService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/traslados")
public class TrasladoController {

    private final TrasladoService trasladoService;

    @PostMapping
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> registrar(@Valid @RequestBody TrasladoRequestDTO dto) {
        log.info("POST /api/v1/traslados - Registrar traslado para interno: {}", dto.getIdInterno());
        TrasladoResponseDTO response = trasladoService.registrar(dto);
        return ResponseEntity.ok(GenericResponseDTO.<TrasladoResponseDTO>builder().response(response).build());
    }

    @GetMapping
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> listarConFiltros(
            @RequestParam(required = false) Long estado,
            @RequestParam(required = false) Long sedeOrigen,
            @RequestParam(required = false) Long sedeDestino,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        log.info("GET /api/v1/traslados - Listar con filtros");
        List<TrasladoResponseDTO> response = trasladoService.listarConFiltros(estado, sedeOrigen, sedeDestino, fechaInicio, fechaFin);
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoResponseDTO>>builder().response(response).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/v1/traslados/{}", id);
        TrasladoResponseDTO response = trasladoService.obtenerPorId(id);
        return ResponseEntity.ok(GenericResponseDTO.<TrasladoResponseDTO>builder().response(response).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TrasladoRequestDTO dto) {
        log.info("PUT /api/v1/traslados/{}", id);
        TrasladoResponseDTO response = trasladoService.actualizar(id, dto);
        return ResponseEntity.ok(GenericResponseDTO.<TrasladoResponseDTO>builder().response(response).build());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoUpdateDTO dto) {
        log.info("PATCH /api/v1/traslados/{}/estado - Nuevo estado: {}", id, dto.getEstadoTrasladoId());
        TrasladoResponseDTO response = trasladoService.cambiarEstado(id, dto);
        return ResponseEntity.ok(GenericResponseDTO.<TrasladoResponseDTO>builder().response(response).build());
    }

    @PatchMapping("/{id}/llegada")
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> registrarLlegada(
            @PathVariable Long id,
            @Valid @RequestBody LlegadaRegistroDTO dto) {
        log.info("PATCH /api/v1/traslados/{}/llegada - Fecha llegada: {}", id, dto.getFechaLlegada());
        TrasladoResponseDTO response = trasladoService.registrarLlegada(id, dto);
        return ResponseEntity.ok(GenericResponseDTO.<TrasladoResponseDTO>builder().response(response).build());
    }

    @GetMapping("/interno/{idInterno}")
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> listarPorInterno(@PathVariable Long idInterno) {
        log.info("GET /api/v1/traslados/interno/{}", idInterno);
        List<TrasladoResponseDTO> response = trasladoService.listarPorInterno(idInterno);
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoResponseDTO>>builder().response(response).build());
    }

    @GetMapping("/sede-origen/{idSede}")
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> listarPorSedeOrigen(@PathVariable Long idSede) {
        log.info("GET /api/v1/traslados/sede-origen/{}", idSede);
        List<TrasladoResponseDTO> response = trasladoService.listarPorSedeOrigen(idSede);
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoResponseDTO>>builder().response(response).build());
    }

    @GetMapping("/sede-destino/{idSede}")
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> listarPorSedeDestino(@PathVariable Long idSede) {
        log.info("GET /api/v1/traslados/sede-destino/{}", idSede);
        List<TrasladoResponseDTO> response = trasladoService.listarPorSedeDestino(idSede);
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoResponseDTO>>builder().response(response).build());
    }

    @GetMapping("/en-curso")
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> listarEnCurso() {
        log.info("GET /api/v1/traslados/en-curso");
        List<TrasladoResponseDTO> response = trasladoService.listarEnCurso();
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoResponseDTO>>builder().response(response).build());
    }

    @GetMapping("/validar/interno/{idInterno}")
    public ResponseEntity<GenericResponseDTO<ValidacionTrasladoActivoDTO>> validarTrasladosActivos(@PathVariable Long idInterno) {
        log.info("GET /api/v1/traslados/validar/interno/{}", idInterno);
        ValidacionTrasladoActivoDTO response = trasladoService.validarTrasladosActivos(idInterno);
        return ResponseEntity.ok(GenericResponseDTO.<ValidacionTrasladoActivoDTO>builder().response(response).build());
    }

    @GetMapping("/validar/sede/{idSede}/capacidad")
    public ResponseEntity<GenericResponseDTO<ValidacionCapacidadDTO>> validarCapacidadSede(@PathVariable Long idSede) {
        log.info("GET /api/v1/traslados/validar/sede/{}/capacidad", idSede);
        ValidacionCapacidadDTO response = trasladoService.validarCapacidadSede(idSede);
        return ResponseEntity.ok(GenericResponseDTO.<ValidacionCapacidadDTO>builder().response(response).build());
    }
}
