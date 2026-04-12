package pe.inpe.ms_traslado.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrasladoResponseDTO {
    private Long idTraslado;
    private Long idInterno;
    private Long sedeOrigenId;
    private Long sedeDestinoId;
    private Long causaId;
    private Long idResolucion;
    private LocalDateTime fechaTraslado;
    private LocalDateTime fechaLlegada;
    private Long estadoTrasladoId;
    private String estadoNombre;
    private String observaciones;
    private List<TrasladoCustodiaResponseDTO> custodias;
}
