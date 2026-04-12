package pe.inpe.ms_traslado.controller;

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
    private final TrasladoCustodiaService trasladoCustodiaService;

    //POST http://localhost:8093/api/v1/custodias/traslados/1/custodias
    @PostMapping("/traslados/{idTraslado}/custodias")
    public ResponseEntity<GenericResponseDTO<TrasladoCustodiaResponseDTO>> addCustodio(
            @PathVariable Long idTraslado,
            @RequestBody TrasladoCustodiaRequestDTO dto){
        log.info("POST /traslados/{}/custodias - Asignar custodio", idTraslado);
        TrasladoCustodiaResponseDTO response = trasladoCustodiaService.addCustodia(idTraslado, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GenericResponseDTO.<TrasladoCustodiaResponseDTO>builder().response(response).build());
    }

    //DELETE http://localhost:8093/api/v1/custodias/traslados/1/custodias/1
    @DeleteMapping("/traslados/{idTraslado}/custodias/{idPersonal}")
    public ResponseEntity<GenericResponseDTO<Void>> removeCustodia(
            @PathVariable Long idTraslado,
            @PathVariable Long idPersonal) {
        log.info("DELETE /traslados/{}/custodias/{} - Remover custodio", idTraslado, idPersonal);
        trasladoCustodiaService.removeCustodia(idTraslado, idPersonal);
        return ResponseEntity.ok(GenericResponseDTO.<Void>builder().response(null).build());
    }

    //GET http://localhost:8093/api/v1/custodias/traslados/1/custodias
    @GetMapping("/traslados/{idTraslado}/custodias")
    public ResponseEntity<GenericResponseDTO<List<TrasladoCustodiaResponseDTO>>> getCustodiasXTraslado(
            @PathVariable Long idTraslado) {
        log.info("GET /traslados/{}/custodias - Listar custodios del traslado", idTraslado);
        List<TrasladoCustodiaResponseDTO> response = trasladoCustodiaService.getCustodiasXTraslado(idTraslado);
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoCustodiaResponseDTO>>builder().response(response).build());
    }

    //GET http://localhost:8093/api/v1/custodias/custodias/personal/1
    @GetMapping("/custodias/personal/{idPersonal}")
    public ResponseEntity<GenericResponseDTO<List<TrasladoResponseDTO>>> getTrasladosXCustodio(
            @PathVariable Long idPersonal) {
        log.info("GET /custodias/personal/{} - Listar traslados por custodio", idPersonal);
        List<TrasladoResponseDTO> response = trasladoCustodiaService.getTrasladosXCustodio(idPersonal);
        return ResponseEntity.ok(GenericResponseDTO.<List<TrasladoResponseDTO>>builder().response(response).build());
    }
}
