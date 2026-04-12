package pe.inpe.ms_traslado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.inpe.ms_traslado.entity.Traslado;
import pe.inpe.ms_traslado.entity.TrasladoCustodia;

import java.util.List;
import java.util.Optional;

public interface TrasladoCustodiaRepository extends JpaRepository<TrasladoCustodia,Long> {

    List<TrasladoCustodia> findByTraslado(Traslado traslado);
    // Buscar por traslado (por ID)
    List<TrasladoCustodia> findByTrasladoIdTraslado(Long idTraslado);

    // Buscar por personal
    List<TrasladoCustodia> findByIdPersonal(Long idPersonal);

    // Buscar por personal y rol
    List<TrasladoCustodia> findByIdPersonalAndRolCustodiaId(Long idPersonal, Long rolCustodiaId);

    // Buscar custodia específica
    Optional<TrasladoCustodia> findByTrasladoAndIdPersonal(Traslado traslado, Long idPersonal);

    // Verificar si existe custodia
    boolean existsByTrasladoAndIdPersonal(Traslado traslado, Long idPersonal);

    // Obtener todos los traslados donde participó un personal (con JPQL)
    @Query(value="SELECT tc.traslado FROM TrasladoCustodia tc WHERE tc.idPersonal = :idPersonal")
    List<Traslado> findTrasladosByPersonal(@Param("idPersonal") Long idPersonal);

    // Obtener traslados activos donde participó un personal
    @Query(value="SELECT tc.traslado FROM TrasladoCustodia tc " +
            "WHERE tc.idPersonal = :idPersonal " +
            "AND tc.traslado.estadoTrasladoId IN :estadosActivos")
    List<Traslado> findTrasladosActivosByPersonal(@Param("idPersonal") Long idPersonal,
                                                  @Param("estadosActivos") List<Long> estadosActivos);

}
