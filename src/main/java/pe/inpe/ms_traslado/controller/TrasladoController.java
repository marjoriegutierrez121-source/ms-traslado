package pe.inpe.ms_traslado.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.inpe.ms_traslado.dto.GenericResponseDTO;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaResponseDTO;
import pe.inpe.ms_traslado.dto.TrasladoRequestDTO;
import pe.inpe.ms_traslado.dto.TrasladoResponseDTO;
import pe.inpe.ms_traslado.service.TrasladoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/traslado")
public class TrasladoController {
    public final TrasladoService trasladoService;
    @PostMapping
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> registrar(
            @RequestBody TrasladoRequestDTO dto) {
        TrasladoResponseDTO response = trasladoService.addTraslado(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GenericResponseDTO.<TrasladoResponseDTO>builder()
                        .response(response)
                        .build());
    }

    // GET /traslados?estado=
    @GetMapping
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> listarPorEstado(
            @RequestParam(defaultValue = "true") Boolean estado) {

        List<TrasladoResponseDTO> response = trasladoService.getTrasladoXEstado(estado);
        return ResponseEntity.ok(
                GenericResponseDTO.<List<TrasladoResponseDTO>>builder()
                        .response(response)
                        .build());
    }

    // GET /traslados/{id}
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> obtenerPorId(
            @PathVariable Long id) {

        TrasladoResponseDTO response = trasladoService.getTrasladoXId(id);
        return ResponseEntity.ok(
                GenericResponseDTO.<TrasladoResponseDTO>builder()
                        .response(response)
                        .build());
    }

    // GET /traslados/interno/{idInterno}
    @GetMapping("/interno/{idInterno}")
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> listarPorInterno(
            @PathVariable Long idInterno) {

        List<TrasladoResponseDTO> response = trasladoService.getTrasladoXIdInterno(idInterno);
        return ResponseEntity.ok(
                GenericResponseDTO.<List<TrasladoResponseDTO>>builder()
                        .response(response)
                        .build());
    }

    // PUT /traslados/{id}
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponseDTO<TrasladoResponseDTO>> actualizar(
            @PathVariable Long id,
            @RequestBody TrasladoRequestDTO dto) {

        TrasladoResponseDTO response = trasladoService.putTraslado(id, dto);
        return ResponseEntity.ok(
                GenericResponseDTO.<TrasladoResponseDTO>builder()
                        .response(response)
                        .build());
    }

    // GET /traslados/{id}/custodia
    @GetMapping("/{id}/custodia")
    public ResponseEntity<GenericResponseDTO<List<TrasladoCustodiaResponseDTO>>> obtenerCustodia(
            @PathVariable Long id) {

        List<TrasladoCustodiaResponseDTO> response = trasladoService.getCustodiaXId(id);
        return ResponseEntity.ok(
                GenericResponseDTO.<List<TrasladoCustodiaResponseDTO>>builder()
                        .response(response)
                        .build());
    }
}
