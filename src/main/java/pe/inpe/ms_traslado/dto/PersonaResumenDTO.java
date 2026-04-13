package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaResumenDTO {
    private Long idPersona;
    private String nombreCompleto;
    private String tipoDocumento;
    private String numeroDocumento;
    private String telefono;
    private String email;
}
