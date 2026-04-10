package pe.inpe.ms_traslado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.inpe.ms_traslado.entity.Traslado;
import pe.inpe.ms_traslado.entity.TrasladoCustodia;

import java.util.List;
import java.util.Optional;

public interface TrasladoCustodiaRepository extends JpaRepository<TrasladoCustodia,Long> {

    List<TrasladoCustodia> findByTraslado(Traslado traslado);
    Optional<TrasladoCustodia> findByIdTrasladoAndIdPersonal(Traslado traslado, Long idPersonal);
}
