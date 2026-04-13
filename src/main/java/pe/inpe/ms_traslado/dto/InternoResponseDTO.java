package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternoResponseDTO {
    private Long idInterno;
    private Long idPersona;
    private String codigoInterno;
    private LocalDate fechaIngreso;
    private Boolean estado;
}