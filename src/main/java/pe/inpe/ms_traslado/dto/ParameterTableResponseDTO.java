package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterTableResponseDTO {
    private Long id;
    private Integer tipo;
    private String codigo;
    private String valor;
    private String descripcion;
    private Boolean activo;
}