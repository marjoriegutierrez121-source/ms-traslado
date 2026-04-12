package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidacionTrasladoDTO {
    private Boolean tieneTrasladoActivo;
    private TrasladoResponseDTO trasladoActivo;
    private String mensaje;
}