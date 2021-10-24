package br.com.fiap.sprint3.repositories;

import br.com.fiap.sprint3.models.Estacionamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstacionamentoRepository extends JpaRepository<Estacionamento, Long> {
}
