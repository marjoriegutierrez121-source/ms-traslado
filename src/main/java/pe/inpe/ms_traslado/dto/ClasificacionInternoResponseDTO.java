package pe.inpe.ms_traslado.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClasificacionInternoResponseDTO {
    private Long idClasificacionInterno;
    private Long idInterno;
    private Long nivelSeguridadId;
    private String nivelSeguridadDescripcion;
    private Long regimenId;
    private String regimenDescripcion;
    private Long grupoId;
    private String grupoDescripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activo;
    private String observaciones;
    private LocalDateTime registrationDate;
    private String registrationUser;
    private LocalDateTime lastModificationDate;
    private String lastModificationUser;
}