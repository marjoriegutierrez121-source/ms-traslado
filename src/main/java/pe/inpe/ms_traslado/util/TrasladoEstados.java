package pe.inpe.ms_traslado.util;

import java.util.List;

public class TrasladoEstados {
    public static final Long PROGRAMADO = 1L;
    public static final Long EN_TRANSITO = 2L;
    public static final Long COMPLETADO = 3L;
    public static final Long CANCELADO = 4L;

    // Listas de estados agrupados
    public static final List<Long> ESTADOS_ACTIVOS = List.of(PROGRAMADO, EN_TRANSITO);
    public static final List<Long> ESTADOS_FINALIZADOS = List.of(COMPLETADO, CANCELADO);

    // Validar si un estado es activo
    public static boolean esActivo(Long estado) {
        return ESTADOS_ACTIVOS.contains(estado);
    }

    // Validar si un estado es finalizado
    public static boolean esFinalizado(Long estado) {
        return ESTADOS_FINALIZADOS.contains(estado);
    }

    // Validar transición permitida entre estados
    public static boolean puedeCambiarA(Long estadoActual, Long nuevoEstado) {
        if (PROGRAMADO.equals(estadoActual)) {
            return EN_TRANSITO.equals(nuevoEstado) || CANCELADO.equals(nuevoEstado);
        }
        if (EN_TRANSITO.equals(estadoActual)) {
            return COMPLETADO.equals(nuevoEstado) || CANCELADO.equals(nuevoEstado);
        }
        return false; // Estados finalizados no pueden cambiar
    }

    // Obtener nombre legible del estado
    public static String getNombre(Long estado) {
        if (PROGRAMADO.equals(estado)) return "PROGRAMADO";
        if (EN_TRANSITO.equals(estado)) return "EN TRÁNSITO";
        if (COMPLETADO.equals(estado)) return "COMPLETADO";
        if (CANCELADO.equals(estado)) return "CANCELADO";
        return "DESCONOCIDO";
    }
}
