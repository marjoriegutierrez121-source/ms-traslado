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
public class TrasladoDetalleResponseDTO {
    private TrasladoResponseDTO traslado;
    private InternoResumenDTO interno;
    private InternoUbicacionResumenDTO ubicacionActual;
    private ClasificacionInternoResumenDTO clasificacionActual;
    private SedeResumenDTO sedeOrigen;
    private SedeResumenDTO sedeDestino;
    private List<TrasladoCustodiaResponseDTO> custodios;
    private ResumenJudicialDTO resumenJudicial;
    private Boolean puedeSerTrasladado;
    private String motivoRestriccion;
    private LocalDateTime fechaConsulta;
}
