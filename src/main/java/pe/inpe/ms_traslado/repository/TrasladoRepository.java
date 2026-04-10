package pe.inpe.ms_traslado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.inpe.ms_traslado.entity.Traslado;

import java.util.List;
import java.util.Optional;

public interface TrasladoRepository extends JpaRepository<Traslado, Long> {

    List<Traslado> findByEstado(Boolean estado);
    List<Traslado> findByIdInterno(Long idInterno);
    List<Traslado> findByEstadoAndSedeOrigenId(Boolean estado, Long sedeOrigenId);
    Optional<Traslado> findByIdInternoAndEstado(Long idInterno, Boolean estado);
}
