package br.com.fiap.sprint3.controlllers.api;

import br.com.fiap.sprint3.models.Pagamento;
import br.com.fiap.sprint3.repositories.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/pagamento")
public class ApiPagamentoController {
    @Autowired
    private PagamentoRepository repository;

    @GetMapping
    @Cacheable("pagamentos")
    public Page<Pagamento> listar(@PageableDefault Pageable pageable){
        return repository.findAll(pageable);

    }

    @PostMapping
    @CacheEvict(value = "pagamentos", allEntries = true)
    public ResponseEntity<Pagamento> create(@RequestBody @Valid Pagamento pagamento, UriComponentsBuilder uriBuilder) {
        repository.save(pagamento);
        URI uri = uriBuilder
                .path("/api/pagamento/{id}")
                .buildAndExpand(pagamento.getId())
                .toUri();

        return ResponseEntity.created(uri).body(pagamento);

    }

    @PutMapping("{id}")
    @CacheEvict(value = "tasks", allEntries = true)
    public ResponseEntity<Pagamento> atualiza(
            @PathVariable Long id,
            @RequestBody Pagamento pagamentoAtualizado
    ){

        Optional<Pagamento> optional = repository.findById(id);

        if (optional.isEmpty())
            return ResponseEntity.notFound().build();

        Pagamento pagamento = optional.get();


        pagamento.setIdCliente(pagamentoAtualizado.getIdCliente());
        pagamento.setCpfTitular(pagamentoAtualizado.getCpfTitular());
        pagamento.setCvv(pagamentoAtualizado.getCvv());
        pagamento.setCpfTitular(pagamentoAtualizado.getCpfTitular());
        pagamento.setMetodo(pagamentoAtualizado.getMetodo());
        pagamento.setNomeTitular(pagamentoAtualizado.getNomeTitular());
        pagamento.setNumeroCartao(pagamentoAtualizado.getNumeroCartao());
        pagamento.setVencimento(pagamentoAtualizado.getVencimento());


        repository.save(pagamento);


        return ResponseEntity.ok(pagamento);

    }

    @DeleteMapping("{id}")
    @CacheEvict(value = "pagamentos", allEntries = true)
    public ResponseEntity<Pagamento> deletar(@PathVariable Long id){
        Optional<Pagamento> pagamento = repository.findById(id);

        if (pagamento.isEmpty())
            return ResponseEntity.notFound().build();

        repository.deleteById(id);
        return ResponseEntity.ok().build();

    }
}
