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
public class SolicitudTrasladoRequestDTO {


    private Long idInterno;

    private Long sedeDestinoId;

    private Long causaId;

    private Long idResolucion;

    private LocalDateTime fechaTraslado;

    private String observaciones;

    private List<TrasladoCustodiaRequestDTO> custodios;
}
