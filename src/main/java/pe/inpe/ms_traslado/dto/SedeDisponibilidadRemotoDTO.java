package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SedeDisponibilidadRemotoDTO {
    private Long idSede;
    private Integer capacidadMaxima;
    private Integer ocupacionActual;
    private Boolean disponible;
}
