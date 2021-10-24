package br.com.fiap.sprint3.controlllers.api;

import br.com.fiap.sprint3.models.Estacionamento;
import br.com.fiap.sprint3.repositories.EstacionamentoRepository;
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
@RequestMapping("/estacionamento")
public class ApiEstacionamentoController {
    @Autowired
    private EstacionamentoRepository repository;

    @GetMapping
    @Cacheable("estacionamentos")
    public Page<Estacionamento> listar(@PageableDefault Pageable pageable){
        return repository.findAll(pageable);

    }

    @PostMapping
    @CacheEvict(value = "estacionamentos", allEntries = true)
    public ResponseEntity<Estacionamento> create(@RequestBody @Valid Estacionamento estacionamento, UriComponentsBuilder uriBuilder) {
        repository.save(estacionamento);
        URI uri = uriBuilder
                .path("/api/estacionamento/{id}")
                .buildAndExpand(estacionamento.getId())
                .toUri();

        return ResponseEntity.created(uri).body(estacionamento);

    }

    @PutMapping("{id}")
    @CacheEvict(value = "tasks", allEntries = true)
    public ResponseEntity<Estacionamento> atualiza(
            @PathVariable Long id,
            @RequestBody Estacionamento estacionamentoAtualizado
    ){

        Optional<Estacionamento> optional = repository.findById(id);

        if (optional.isEmpty())
            return ResponseEntity.notFound().build();

        Estacionamento estacionamento = optional.get();

        estacionamento.setNome(estacionamentoAtualizado.getNome());
        estacionamento.setValor(estacionamentoAtualizado.getValor());


        repository.save(estacionamento);


        return ResponseEntity.ok(estacionamento);

    }

    @DeleteMapping("{id}")
    @CacheEvict(value = "estacionamentos", allEntries = true)
    public ResponseEntity<Estacionamento> deletar(@PathVariable Long id){
        Optional<Estacionamento> estacionamento = repository.findById(id);

        if (estacionamento.isEmpty())
            return ResponseEntity.notFound().build();

        repository.deleteById(id);
        return ResponseEntity.ok().build();

    }

}
