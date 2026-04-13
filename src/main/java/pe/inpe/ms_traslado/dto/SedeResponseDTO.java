package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SedeResponseDTO {
    private Long id;
    private String nombre;
    private String codigo;
    private String direccion;
    private Long idInstituto;
    private String nivelSeguridad;
    private Boolean activo;
}