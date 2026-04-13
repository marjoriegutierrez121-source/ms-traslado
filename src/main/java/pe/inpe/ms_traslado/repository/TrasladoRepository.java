package pe.inpe.ms_traslado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.inpe.ms_traslado.entity.Traslado;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrasladoRepository extends JpaRepository<Traslado, Long> {
    List<Traslado> findByEstadoTrasladoIdIn(List<Long> estadoTrasladoIds);
    List<Traslado> findByIdInterno(Long idInterno);
    List<Traslado> findBySedeOrigenId(Long sedeOrigenId);
    List<Traslado> findBySedeDestinoId(Long sedeDestinoId);
    List<Traslado> findBySedeDestinoIdAndEstadoTrasladoIdIn(Long sede, List<Long> estados);
    @Query("SELECT t FROM Traslado t WHERE " +
            "(:estado IS NULL OR t.estadoTrasladoId = :estado) AND " +
            "(:sedeOrigen IS NULL OR t.sedeOrigenId = :sedeOrigen) AND " +
            "(:sedeDestino IS NULL OR t.sedeDestinoId = :sedeDestino) AND " +
            "(:fechaInicio IS NULL OR t.fechaTraslado >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR t.fechaTraslado <= :fechaFin)")
    List<Traslado> findByFiltros(@Param("estado") Long estado,
                                 @Param("sedeOrigen") Long sedeOrigen,
                                 @Param("sedeDestino") Long sedeDestino,
                                 @Param("fechaInicio") LocalDateTime fechaInicio,
                                 @Param("fechaFin") LocalDateTime fechaFin);

    boolean existsByIdInternoAndEstadoTrasladoIdIn(Long idInterno, List<Long> estadosActivos);
    List<Traslado> findByIdInternoOrderByFechaTrasladoDesc(Long idInterno);
}
