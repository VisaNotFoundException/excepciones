package ejemplo.entity;

import ejemplo.dto.Roles;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name="fecha_creacion", nullable = false)
    private OffsetDateTime fechaCreacion = OffsetDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles rol = Roles.USUARIO;

    public Usuario() {}

    public Usuario(String nombreCompleto, String email, Roles rol) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.rol = rol != null ? rol : Roles.USUARIO;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public OffsetDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(OffsetDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Roles getRol() { return rol; }
    public void setRol(Roles rol) {  this.rol = rol != null ? rol : Roles.USUARIO; }

    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) fechaCreacion = OffsetDateTime.now();
        if (rol == null) rol = Roles.USUARIO;
    }
}
