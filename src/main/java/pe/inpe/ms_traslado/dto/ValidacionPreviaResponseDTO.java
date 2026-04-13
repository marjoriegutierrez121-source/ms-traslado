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
public class ValidacionPreviaResponseDTO {
    private Long idInterno;
    private Boolean puedeSerTrasladado;
    private List<ValidacionItemDTO> validaciones;
    private ResumenJudicialDTO restriccionesJudiciales;
    private ValidacionCapacidadDTO capacidadSede;
    private Long sedeActualId;
}
