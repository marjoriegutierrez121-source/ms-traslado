package pe.inpe.ms_traslado.util;

import java.util.List;

public final class GeneralConstants {

    private GeneralConstants() {}

    // Estados de Traslado
    public static final Long ESTADO_TRASLADO_PROGRAMADO  = 1L;
    public static final Long ESTADO_TRASLADO_EN_TRANSITO = 2L;
    public static final Long ESTADO_TRASLADO_COMPLETADO  = 3L;
    public static final Long ESTADO_TRASLADO_CANCELADO   = 4L;

    public static final List<Long> ESTADOS_TRASLADO_ACTIVOS =
            List.of(ESTADO_TRASLADO_PROGRAMADO, ESTADO_TRASLADO_EN_TRANSITO);

    // Tipos de parámetros en ms-core
    public static final Integer TIPO_CAUSA_TRASLADO = 101;
    public static final Integer TIPO_RESOLUCION = 102;
    public static final Integer TIPO_ROL_CUSTODIA = 103;
}

