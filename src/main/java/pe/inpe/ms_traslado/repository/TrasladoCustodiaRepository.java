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

    List<TrasladoCustodia> findByTrasladoIdTraslado(Long idTraslado);

    List<TrasladoCustodia> findByIdPersonal(Long idPersonal);

    List<TrasladoCustodia> findByIdPersonalAndRolCustodiaId(Long idPersonal, Long rolCustodiaId);

    Optional<TrasladoCustodia> findByTrasladoAndIdPersonal(Traslado traslado, Long idPersonal);

    boolean existsByTrasladoAndIdPersonal(Traslado traslado, Long idPersonal);

    @Query(value="SELECT tc.traslado FROM TrasladoCustodia tc WHERE tc.idPersonal = :idPersonal")
    List<Traslado> findTrasladosByPersonal(@Param("idPersonal") Long idPersonal);

    @Query(value="SELECT tc.traslado FROM TrasladoCustodia tc " +
            "WHERE tc.idPersonal = :idPersonal " +
            "AND tc.traslado.estadoTrasladoId IN :estadosActivos")
    List<Traslado> findTrasladosActivosByPersonal(@Param("idPersonal") Long idPersonal,
                                                  @Param("estadosActivos") List<Long> estadosActivos);

}
