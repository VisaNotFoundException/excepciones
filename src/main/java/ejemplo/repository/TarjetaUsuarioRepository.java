package ejemplo.repository;

import ejemplo.entity.TarjetaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarjetaUsuarioRepository extends JpaRepository<TarjetaUsuario, Long> {

    boolean existsByToken(String token);
}
