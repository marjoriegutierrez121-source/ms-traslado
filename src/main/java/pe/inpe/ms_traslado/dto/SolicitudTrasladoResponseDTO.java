package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudTrasladoResponseDTO {
    private Long idTraslado;
    private Long estadoTrasladoId;
    private LocalDateTime fechaTraslado;
    private Long sedeOrigenId;
    private Long sedeDestinoId;
    private String mensaje;
}