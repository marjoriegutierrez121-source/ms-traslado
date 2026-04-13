package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternoUbicacionResponseDTO {
    private Long idInternoUbicacion;
    private Long idInterno;
    private Long idInstitutoSede;
    private String nombreSede;
    private String codigoSede;
    private Long idPabellon;
    private String nombrePabellon;
    private Long idCelda;
    private String codigoCelda;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaFin;
    private Boolean activo;
    private String observaciones;
    private LocalDateTime registrationDate;
    private String registrationUser;
    private LocalDateTime lastModificationDate;
    private String lastModificationUser;
}
