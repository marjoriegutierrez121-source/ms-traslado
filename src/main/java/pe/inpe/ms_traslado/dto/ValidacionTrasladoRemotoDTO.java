package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidacionTrasladoRemotoDTO {
    private Boolean puedeSerTrasladado;
    private String motivoRestriccion;
}
