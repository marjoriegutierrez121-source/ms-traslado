package pe.inpe.ms_traslado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.inpe.ms_traslado.entity.Traslado;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface TrasladoRepository extends JpaRepository<Traslado, Long> {

    List<Traslado> findByEstadoTrasladoId(Long estadoTrasladoId);
    List<Traslado> findByIdInterno(Long idInterno);

    List<Traslado> findByEstadoTrasladoIdAndSedeOrigenId(Long estadoTrasladoId, Long sedeOrigenId);
    Optional<Traslado> findByIdInternoAndEstadoTrasladoId(Long idInterno, Long estadoTrasladoId);

    List<Traslado> findBySedeOrigenId(Long sedeOrigenId);
    List<Traslado> findBySedeDestinoId(Long sedeDestinoId);
    List<Traslado> findBySedeOrigenIdAndEstadoTrasladoId(Long sedeOrigenId, Long estadoTrasladoId);
    List<Traslado> findBySedeDestinoIdAndEstadoTrasladoId(Long sedeDestinoId, Long estadoTrasladoId);
    List<Traslado> findByEstadoTrasladoIdIn(List<Long> estados);
    List<Traslado> findBySedeDestinoIdAndEstadoTrasladoIdIn(Long sede, List<Long> estados);

    default List<Traslado> findTrasladosActivos() {
        return findByEstadoTrasladoIdIn(List.of(1L, 2L));
    }

    List<Traslado> findByFechaTrasladoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Traslado> findByFechaLlegadaIsNullAndEstadoTrasladoIdIn(List<Long> estados);
    List<Traslado> findByFechaLlegadaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /*@Query(value ="SELECT t FROM Traslado t WHERE " +
            "(:estado IS NULL OR t.estadoTrasladoId = :estado) AND " +
            "(:sedeOrigen IS NULL OR t.sedeOrigenId = :sedeOrigen) AND " +
            "(:sedeDestino IS NULL OR t.sedeDestinoId = :sedeDestino) AND " +
            "(:fechaInicio IS NULL OR t.fechaTraslado >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR t.fechaTraslado <= :fechaFin)")
    List<Traslado> findByFiltros(@Param("estado") Long estado,
                                 @Param("sedeOrigen") Long sedeOrigen,
                                 @Param("sedeDestino") Long sedeDestino,
                                 @Param("fechaInicio") LocalDateTime fechaInicio,
                                 @Param("fechaFin") LocalDateTime fechaFin);*/
    default List<Traslado> findByFiltros(Long estado, Long sedeOrigen, Long sedeDestino,
                                         LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return findAll().stream()
                .filter(t -> estado == null || t.getEstadoTrasladoId().equals(estado))
                .filter(t -> sedeOrigen == null || t.getSedeOrigenId().equals(sedeOrigen))
                .filter(t -> sedeDestino == null || t.getSedeDestinoId().equals(sedeDestino))
                .filter(t -> fechaInicio == null || !t.getFechaTraslado().isBefore(fechaInicio))
                .filter(t -> fechaFin == null || !t.getFechaTraslado().isAfter(fechaFin))
                .collect(Collectors.toList());
    }

    boolean existsByIdInternoAndEstadoTrasladoIdIn(Long idInterno, List<Long> estadosActivos);
    long countBySedeOrigenIdAndEstadoTrasladoIdIn(Long sedeOrigenId, List<Long> estadosActivos);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Traslado t " +
            "WHERE t.idInterno = :idInterno AND t.estadoTrasladoId IN :estadosActivos")
    boolean existeTrasladoActivo(@Param("idInterno") Long idInterno,
                                 @Param("estadosActivos") List<Long> estadosActivos);

    @Query("SELECT t FROM Traslado t WHERE t.idInterno = :idInterno " +
            "AND t.estadoTrasladoId IN :estadosActivos ORDER BY t.fechaTraslado DESC")
    Optional<Traslado> findTrasladoActivoByInterno(@Param("idInterno") Long idInterno,
                                                   @Param("estadosActivos") List<Long> estadosActivos);

}
