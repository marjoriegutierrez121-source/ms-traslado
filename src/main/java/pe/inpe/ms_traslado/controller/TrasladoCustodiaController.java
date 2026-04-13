package pe.inpe.ms_traslado.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.inpe.ms_traslado.dto.GenericResponseDTO;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaRequestDTO;
import pe.inpe.ms_traslado.dto.TrasladoCustodiaResponseDTO;
import pe.inpe.ms_traslado.dto.TrasladoResponseDTO;
import pe.inpe.ms_traslado.service.TrasladoCustodiaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/custodias")
public class TrasladoCustodiaController {
    private final TrasladoCustodiaService custodiaService;

    @PostMapping("/{idTraslado}/custodias")
    public ResponseEntity<GenericResponseDTO<TrasladoCustodiaResponseDTO>> asignarCustodia(
            @PathVariable Long idTraslado,
            @Valid @RequestBody TrasladoCustodiaRequestDTO dto) {
        log.info("POST /api/v1/traslados/{}/custodias - Personal: {}", idTraslado, dto.getIdPersonal());
        TrasladoCustodiaResponseDTO response = custodiaService.asignarCustodia(idTraslado, dto);
        return ResponseEntity.ok(GenericResponseDTO.<TrasladoCustodiaResponseDTO>builder().response(response).build());
    }

    @DeleteMapping("/{idTraslado}/custodias/{idPersonal}")
    public ResponseEntity<GenericResponseDTO<Void>> removerCustodia(
            @PathVariable Long idTraslado,
            @PathVariable Long idPersonal) {
        log.info("DELETE /api/v1/traslados/{}/custodias/{}", idTraslado, idPersonal);
        custodiaService.removerCustodia(idTraslado, idPersonal);
        return ResponseEntity.ok(GenericResponseDTO.<Void>builder().response(null).build());
    }

    @GetMapping("/{idTraslado}/custodias")
    public ResponseEntity<GenericResponseDTO<List<TrasladoCustodiaResponseDTO>>> listarPorTraslado(@PathVariable Long idTraslado) {
        log.info("GET /api/v1/traslados/{}/custodias", idTraslado);
        List<TrasladoCustodiaResponseDTO> response = custodiaService.listarPorTraslado(idTraslado);
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoCustodiaResponseDTO>>builder().response(response).build());
    }

    @GetMapping("/custodias/personal/{idPersonal}")
    public ResponseEntity<GenericResponseDTO<List<TrasladoCustodiaResponseDTO>>> listarPorPersonal(@PathVariable Long idPersonal) {
        log.info("GET /api/v1/custodias/personal/{}", idPersonal);
        List<TrasladoCustodiaResponseDTO> response = custodiaService.listarPorPersonal(idPersonal);
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoCustodiaResponseDTO>>builder().response(response).build());
    }
}
