package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClasificacionInternoResumenDTO {
    private Long idClasificacion;
    private Long nivelSeguridadId;
    private String nivelSeguridadDescripcion;
    private Boolean clasificacionActiva;
}
