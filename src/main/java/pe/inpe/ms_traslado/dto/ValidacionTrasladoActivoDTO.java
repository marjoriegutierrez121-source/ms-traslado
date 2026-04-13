package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidacionTrasladoActivoDTO {
    private Long idInterno;
    private Boolean tieneTrasladoActivo;
    private Long idTrasladoActivo;
    private Long estadoActual;
}