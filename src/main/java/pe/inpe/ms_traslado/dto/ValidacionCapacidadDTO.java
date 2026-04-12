package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidacionCapacidadDTO {
    private Long sedeId;
    private Integer capacidadTotal;
    private Integer internosActuales;
    private Integer disponibles;
    private Boolean puedeRecibir;
    private String mensaje;
}
