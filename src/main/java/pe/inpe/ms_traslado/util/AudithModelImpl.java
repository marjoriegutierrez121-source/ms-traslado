package pe.inpe.ms_traslado.util;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AudithModelImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {return Optional.of("SYSTEM");}
}
