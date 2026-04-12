package pe.inpe.ms_traslado.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import pe.inpe.ms_traslado.util.AuditModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "traslado")
@EqualsAndHashCode(callSuper = false)
public class Traslado extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_traslado")
    private Long idTraslado;

    @Column(name = "id_interno", nullable = false)
    private Long idInterno;

    @Column(name = "sede_origen_id")
    private Long sedeOrigenId;

    @Column(name = "sede_destino_id")
    private Long sedeDestinoId;

    @Column(name = "causa_id")
    private Long causaId;

    @Column(name = "id_resolucion")
    private Long idResolucion;

    @Column(name = "fecha_traslado")
    private LocalDateTime fechaTraslado;

    @Column(name = "fecha_llegada")
    private LocalDateTime fechaLlegada;

    @Column(name = "estado_traslado_id")
    private Long estadoTrasladoId;

    @Column(name = "observaciones")
    private String observaciones;

    @OneToMany(mappedBy = "traslado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrasladoCustodia> custodias = new ArrayList<>();
}
