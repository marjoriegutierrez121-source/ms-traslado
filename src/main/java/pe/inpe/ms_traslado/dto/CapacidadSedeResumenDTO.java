package pe.inpe.ms_traslado.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CapacidadSedeResumenDTO {
    private Long idSede;
    private Integer capacidadMaxima;
    private Integer ocupacionActual;
    private Integer cuposDisponibles;
}
