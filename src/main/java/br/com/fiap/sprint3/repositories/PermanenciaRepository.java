package br.com.fiap.sprint3.repositories;

import br.com.fiap.sprint3.models.Permanencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermanenciaRepository extends JpaRepository<Permanencia, Long> {
}
