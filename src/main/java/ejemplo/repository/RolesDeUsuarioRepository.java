package ejemplo.repository;

import ejemplo.dto.Roles;
import ejemplo.entity.RolesDeUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesDeUsuarioRepository extends JpaRepository<RolesDeUsuario, Long> {
    Optional<RolesDeUsuario> findByNombre(Roles nombre);
}
