package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestriccionesTrasladoDTO {
    private Boolean puedeSerTrasladado;
    private List<String> restricciones;
    private String nivelRestriccion;
    private String mensajeAdicional;
}
