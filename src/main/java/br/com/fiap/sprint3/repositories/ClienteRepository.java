package br.com.fiap.sprint3.repositories;


import br.com.fiap.sprint3.models.Cliente;
import br.com.fiap.sprint3.models.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
