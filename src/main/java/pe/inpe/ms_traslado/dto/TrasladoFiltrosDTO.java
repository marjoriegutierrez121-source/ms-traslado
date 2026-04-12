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
public class TrasladoFiltrosDTO {
    private Long estadoTrasladoId;
    private Long sedeOrigenId;
    private Long sedeDestinoId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}