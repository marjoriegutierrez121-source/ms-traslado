package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CambioEstadoDTO {
    private Long estadoTrasladoId;  // 2=En tránsito, 3=Completado, 4=Cancelado
    private String observaciones;
}
