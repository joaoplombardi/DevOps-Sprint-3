package br.com.fiap.sprint3.repositories;

import br.com.fiap.sprint3.models.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
