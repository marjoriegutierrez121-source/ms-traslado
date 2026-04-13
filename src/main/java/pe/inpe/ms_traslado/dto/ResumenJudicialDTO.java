package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenJudicialDTO {
    private Boolean tieneExpedientes;
    private Integer cantidadExpedientes;
    private Boolean tieneMandatoActivo;
    private String tipoMandato;
    private LocalDateTime fechaFinMandato;
    private Boolean tieneRestricciones;
    private List<String> restricciones;
    private String nivelRestriccion;
}
