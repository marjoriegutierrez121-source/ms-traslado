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
public class MandatoActivoDTO {
    private Long idMandato;
    private String tipoMandato;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean restringeTraslado;
}
