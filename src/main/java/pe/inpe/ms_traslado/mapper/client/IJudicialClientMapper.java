package pe.inpe.ms_traslado.mapper.client;

import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pe.inpe.ms_traslado.dto.ExpedienteResponseDTO;
import pe.inpe.ms_traslado.dto.MandatoActivoDTO;
import pe.inpe.ms_traslado.dto.RestriccionesTrasladoDTO;
import pe.inpe.ms_traslado.dto.ResumenJudicialDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface IJudicialClientMapper {
    /**
     * Convierte lista de expedientes y mandato activo a ResumenJudicialDTO
     */
    @Mapping(target = "tieneExpedientes", expression = "java(expedientes != null && !expedientes.isEmpty())")
    @Mapping(target = "cantidadExpedientes", expression = "java(expedientes != null ? expedientes.size() : 0)")
    @Mapping(target = "tieneMandatoActivo", expression = "java(mandatoActivo != null)")
    @Mapping(target = "tipoMandato", source = "mandatoActivo.tipoMandato")
    @Mapping(target = "fechaFinMandato", source = "mandatoActivo.fechaFin")
    @Mapping(target = "tieneRestricciones", source = "mandatoActivo", qualifiedByName = "calcularTieneRestricciones")
    @Mapping(target = "restricciones", source = "mandatoActivo", qualifiedByName = "obtenerRestricciones")
    @Mapping(target = "nivelRestriccion", source = "mandatoActivo", qualifiedByName = "calcularNivelRestriccion")
    ResumenJudicialDTO toResumenJudicial(List<ExpedienteResponseDTO> expedientes, MandatoActivoDTO mandatoActivo);

    /**
     * Versión con RestriccionesTrasladoDTO (para validación previa)
     */
    @Mapping(target = "tieneExpedientes", source = "expedientes", qualifiedByName = "hasExpedientes")
    @Mapping(target = "cantidadExpedientes", source = "expedientes", qualifiedByName = "countExpedientes")
    @Mapping(target = "tieneMandatoActivo", constant = "false")
    @Mapping(target = "tipoMandato", constant = "null")
    @Mapping(target = "fechaFinMandato", constant = "null")
    @Mapping(target = "tieneRestricciones", expression = "java(restricciones != null && restricciones.getRestricciones() != null && !restricciones.getRestricciones().isEmpty())")
    @Mapping(target = "restricciones", source = "restricciones.restricciones")
    @Mapping(target = "nivelRestriccion", source = "restricciones.nivelRestriccion")
    ResumenJudicialDTO toResumenJudicialFromRestricciones(
            List<ExpedienteResponseDTO> expedientes,
            RestriccionesTrasladoDTO restricciones);

    /**
     * Retorna un ResumenJudicialDTO vacío (sin restricciones)
     */
    default ResumenJudicialDTO emptyResumenJudicial() {
        return ResumenJudicialDTO.builder()
                .tieneExpedientes(false)
                .cantidadExpedientes(0)
                .tieneMandatoActivo(false)
                .tieneRestricciones(false)
                .restricciones(new ArrayList<>())
                .nivelRestriccion("NINGUNO")
                .build();
    }

    // ========== MÉTODOS NAMED ==========

    @Named("calcularTieneRestricciones")
    default Boolean calcularTieneRestricciones(MandatoActivoDTO mandatoActivo) {
        if (mandatoActivo == null) return false;
        return Boolean.TRUE.equals(mandatoActivo.getRestringeTraslado());
    }

    @Named("obtenerRestricciones")
    default List<String> obtenerRestricciones(MandatoActivoDTO mandatoActivo) {
        if (mandatoActivo == null || !Boolean.TRUE.equals(mandatoActivo.getRestringeTraslado())) {
            return new ArrayList<>();
        }
        List<String> restricciones = new ArrayList<>();
        if (mandatoActivo.getTipoMandato() != null) {
            restricciones.add("Mandato " + mandatoActivo.getTipoMandato() + " activo");
        }
        if (mandatoActivo.getFechaFin() != null && mandatoActivo.getFechaFin().isBefore(LocalDateTime.now())) {
            restricciones.add("Mandato vencido - requiere renovación");
        }
        return restricciones;
    }

    @Named("calcularNivelRestriccion")
    default String calcularNivelRestriccion(MandatoActivoDTO mandatoActivo) {
        if (mandatoActivo == null) return "NINGUNO";
        if (!Boolean.TRUE.equals(mandatoActivo.getRestringeTraslado())) return "NINGUNO";

        String tipo = mandatoActivo.getTipoMandato();
        if (tipo != null) {
            if (tipo.contains("PREVENTIVA") || tipo.contains("PRISION")) {
                return "ALTO";
            }
            if (tipo.contains("DOMICILIARIA")) {
                return "MEDIO";
            }
        }
        return "BAJO";
    }

    @Named("hasExpedientes")
    default Boolean hasExpedientes(List<ExpedienteResponseDTO> expedientes) {
        return expedientes != null && !expedientes.isEmpty();
    }

    @Named("countExpedientes")
    default Integer countExpedientes(List<ExpedienteResponseDTO> expedientes) {
        return expedientes != null ? expedientes.size() : 0;
    }
}
