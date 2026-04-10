package pe.inpe.ms_traslado.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import pe.inpe.ms_traslado.util.AuditModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "traslado_custodia")
public class TrasladoCustodia extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_traslado_custodia")
    private Long idTrasladoCustodia;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_traslado", nullable = false)
    private Traslado traslado;

    @Column(name = "id_personal")
    private Long idPersonal;

    @Column(name = "rol_custodia_id")
    private Long rolCustodiaId;

}
