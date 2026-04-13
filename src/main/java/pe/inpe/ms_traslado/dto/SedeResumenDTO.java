package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SedeResumenDTO {
    private Long idSede;
    private String nombreSede;
    private String codigoSede;
    private String nivelSeguridad;
    private String direccion;
    private Boolean activo;
}
