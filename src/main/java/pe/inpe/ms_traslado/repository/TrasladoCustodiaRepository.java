package pe.inpe.ms_traslado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.inpe.ms_traslado.entity.Traslado;
import pe.inpe.ms_traslado.entity.TrasladoCustodia;

import java.util.List;
import java.util.Optional;

public interface TrasladoCustodiaRepository extends JpaRepository<TrasladoCustodia,Long> {
    List<TrasladoCustodia> findByTrasladoIdTraslado(Long idTraslado);
    List<TrasladoCustodia> findByIdPersonal(Long idPersonal);
    boolean existsByTrasladoIdTrasladoAndIdPersonal(Long idTraslado, Long idPersonal);
    Optional<TrasladoCustodia> findByTrasladoIdTrasladoAndIdPersonal(Long idTraslado, Long idPersonal);
    List<TrasladoCustodia> findByIdPersonalAndRolCustodiaId(Long idPersonal, Long rolCustodiaId);
    List<TrasladoCustodia> findByTrasladoIdTrasladoAndRolCustodiaId(Long idTraslado, Long rolCustodiaId);
    void deleteByTrasladoIdTrasladoAndIdPersonal(Long idTraslado, Long idPersonal);
}
