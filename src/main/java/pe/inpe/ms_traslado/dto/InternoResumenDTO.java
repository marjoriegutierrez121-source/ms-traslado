package pe.inpe.ms_traslado.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternoResumenDTO {
    private Long idInterno;
    private Long idPersona;
    private String codigoInterno;
    private Boolean activo;
}
