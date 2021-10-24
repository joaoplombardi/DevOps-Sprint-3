package br.com.fiap.sprint3.controlllers.api;

import br.com.fiap.sprint3.models.Permanencia;
import br.com.fiap.sprint3.repositories.PermanenciaRepository;
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
@RequestMapping("/permanencia")
public class ApiPermanenciaController {
    @Autowired
    private PermanenciaRepository repository;

    @GetMapping
    @Cacheable("permanencia")
    public Page<Permanencia> listar(@PageableDefault Pageable pageable){
        return repository.findAll(pageable);

    }

    @PostMapping
    @CacheEvict(value = "permanencia", allEntries = true)
    public ResponseEntity<Permanencia> create(@RequestBody @Valid Permanencia permanencia, UriComponentsBuilder uriBuilder) {
        repository.save(permanencia);
        URI uri = uriBuilder
                .path("/api/permanencia/{id}")
                .buildAndExpand(permanencia.getId())
                .toUri();

        return ResponseEntity.created(uri).body(permanencia);

    }

    @PutMapping("{id}")
    @CacheEvict(value = "permanencia", allEntries = true)
    public ResponseEntity<Permanencia> atualiza(
            @PathVariable Long id,
            @RequestBody Permanencia permanenciaAtualizado
    ){

        Optional<Permanencia> optional = repository.findById(id);

        if (optional.isEmpty())
            return ResponseEntity.notFound().build();

        Permanencia permanencia = optional.get();

        permanencia.setIdCliente(permanenciaAtualizado.getIdCliente());
        permanencia.setDtEntrada(permanenciaAtualizado.getDtEntrada());
        permanencia.setDtSaida(permanenciaAtualizado.getDtSaida());
        permanencia.setIdEstacionamento(permanenciaAtualizado.getIdEstacionamento());


        repository.save(permanencia);


        return ResponseEntity.ok(permanencia);

    }

    @DeleteMapping("{id}")
    @CacheEvict(value = "permanencia", allEntries = true)
    public ResponseEntity<Permanencia> deletar(@PathVariable Long id){
        Optional<Permanencia> permanencia = repository.findById(id);

        if (permanencia.isEmpty())
            return ResponseEntity.notFound().build();

        repository.deleteById(id);
        return ResponseEntity.ok().build();

    }
}
