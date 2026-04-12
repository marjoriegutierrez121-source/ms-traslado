package pe.inpe.ms_traslado.controller;

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
    public final TrasladoService trasladoService;

    //POST http://localhost:8093/api/v1/traslados
    @PostMapping
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> addTraslado(
            @RequestBody TrasladoRequestDTO dto) {
        log.info("Insertando nuevo traslado");
        TrasladoResponseDTO response = trasladoService.addTraslado(dto);
        GenericResponseDTO<TrasladoResponseDTO> genericResponseDTO = GenericResponseDTO.<TrasladoResponseDTO>builder()
                .response(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(genericResponseDTO);
    }

    //GET http://localhost:8093/api/v1/traslados?estado=1
    @GetMapping
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> getTraslados(
            @RequestParam(required = false) Long estado,
            @RequestParam(required = false) Long sedeOrigen,
            @RequestParam(required = false) Long sedeDestino,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        log.info("GET /traslados - Listar traslados con filtros");
        List<TrasladoResponseDTO> response = trasladoService.getTrasladosConFiltros(estado, sedeOrigen, sedeDestino, fechaInicio, fechaFin);
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoResponseDTO>>builder().response(response).build());
    }

    //GET http://localhost:8093/api/v1/traslados/1
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> getTrasladoXId(
            @PathVariable Long id) {
        log.info("GET /traslados/{} - Obtener detalle de traslado", id);
        TrasladoResponseDTO response = trasladoService.getTrasladoXId(id);
        return ResponseEntity.ok(GenericResponseDTO.<TrasladoResponseDTO>builder().response(response).build());
    }

    //PUT http://localhost:8093/api/v1/traslados/2
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> putTraslado(
            @PathVariable Long id, @RequestBody TrasladoRequestDTO dto) {
        log.info("PUT /traslados/{} - Actualizar traslado", id);
        TrasladoResponseDTO response = trasladoService.putTraslado(id, dto);
        return ResponseEntity.ok(GenericResponseDTO.<TrasladoResponseDTO>builder().response(response).build());
    }

    //PATCH http://localhost:8093/api/v1/traslados/2/estado  (HACER BODY)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> patchCambiarEstado(
            @PathVariable Long id, @RequestBody CambioEstadoDTO dto) {
        log.info("PATCH /traslados/{}/estado - Cambiar estado", id);
        TrasladoResponseDTO response = trasladoService.patchCambiarEstado(id, dto);
        return ResponseEntity.ok(GenericResponseDTO.<TrasladoResponseDTO>builder().response(response).build());
    }

    //PATCH http://localhost:8093/api/v1/traslados/2/llegada (BODY)
    @PatchMapping("/{id}/llegada")
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> patchRegistrarLlegada(
            @PathVariable Long id, @RequestBody RegistroLlegadaDTO dto) {
        log.info("PATCH /traslados/{}/llegada - Registrar llegada", id);
        TrasladoResponseDTO response = trasladoService.patchRegistrarLlegada(id, dto);
        return ResponseEntity.ok(GenericResponseDTO.<TrasladoResponseDTO>builder().response(response).build());
    }

    // GET http://localhost:8093/api/v1/traslados/interno/1
    @GetMapping("/interno/{idInterno}")
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> getTrasladosXIdInterno(
            @PathVariable Long idInterno) {
        log.info("GET /traslados/interno/{} - Listar historial de traslados", idInterno);
        List<TrasladoResponseDTO> response = trasladoService.getTrasladosXIdInterno(idInterno);
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoResponseDTO>>builder().response(response).build());
    }

    //GET http://localhost:8093/api/v1/traslados/sede-origen/1
    @GetMapping("/sede-origen/{idSede}")
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> getTrasladosXSedeOrigen(
            @PathVariable Long idSede) {
        log.info("GET /traslados/sede-origen/{} - Listar traslados por sede origen", idSede);
        List<TrasladoResponseDTO> response = trasladoService.getTrasladosXSedeOrigen(idSede);
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoResponseDTO>>builder().response(response).build());
    }

    //GET http://localhost:8093/api/v1/traslados/sede-destino/1
    @GetMapping("/sede-destino/{idSede}")
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> getTrasladosXSedeDestino(
            @PathVariable Long idSede) {
        log.info("GET /traslados/sede-destino/{} - Listar traslados por sede destino", idSede);
        List<TrasladoResponseDTO> response = trasladoService.getTrasladosXSedeDestino(idSede);
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoResponseDTO>>builder().response(response).build());
    }

    //GET http://localhost:8093/api/v1/traslados/en-curso
    @GetMapping("/en-curso")
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> getTrasladosEnCurso() {
        log.info("GET /traslados/en-curso - Listar traslados en tránsito");
        List<TrasladoResponseDTO> response = trasladoService.getTrasladosEnCurso();
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoResponseDTO>>builder().response(response).build());
    }

    //GET http://localhost:8093/api/v1/traslados/validar/interno/1
    @GetMapping("/validar/interno/{idInterno}")
    public ResponseEntity<GenericResponseDTO<ValidacionTrasladoDTO>> validarTrasladoActivo(
            @PathVariable Long idInterno) {
        log.info("GET /traslados/validar/interno/{} - Validar traslado activo", idInterno);
        ValidacionTrasladoDTO response = trasladoService.validarTrasladoActivo(idInterno);
        return ResponseEntity.ok(GenericResponseDTO.<ValidacionTrasladoDTO>builder().response(response).build());
    }

    //GET http://localhost:8093/api/v1/traslados/validar/sede/1/capacidad
    @GetMapping("/validar/sede/{idSede}/capacidad")
    public ResponseEntity<GenericResponseDTO<ValidacionCapacidadDTO>> verificarCapacidadSede(
            @PathVariable Long idSede) {
        log.info("GET /traslados/validar/sede/{}/capacidad - Verificar capacidad de sede", idSede);
        ValidacionCapacidadDTO response = trasladoService.verificarCapacidadSede(idSede);
        return ResponseEntity.ok(GenericResponseDTO.<ValidacionCapacidadDTO>builder().response(response).build());
    }
}
