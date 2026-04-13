package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpedienteResponseDTO {
    private Long idExpediente;
    private String numeroExpediente;
    private String estado;
    private String juzgado;
}
