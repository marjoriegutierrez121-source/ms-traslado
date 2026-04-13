package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametroResumenDTO {
    private Long idParametro;
    private String codigoParametro;
    private String valorParametro;
    private String descripcionParametro;
}