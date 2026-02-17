package ejemplo.entity;

import ejemplo.dto.Roles;
import jakarta.persistence.*;

@Entity
@Table(name = "roles_de_usuario")
public class RolesDeUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Roles nombre;

    public RolesDeUsuario() {}

    public RolesDeUsuario(Roles nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public Roles getNombre() {
        return nombre;
    }

    public void setNombre(Roles nombre) {
        this.nombre = nombre;
    }
}
