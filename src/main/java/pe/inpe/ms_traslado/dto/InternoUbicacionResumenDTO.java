package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternoUbicacionResumenDTO {
    private Long idInternoUbicacion;
    private Long idInterno;
    private Long idSede;
    private String nombreSede;
    private Boolean ubicacionActiva;
}
