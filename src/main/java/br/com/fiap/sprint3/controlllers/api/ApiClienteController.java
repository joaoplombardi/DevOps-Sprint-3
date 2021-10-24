package br.com.fiap.sprint3.controlllers.api;

import br.com.fiap.sprint3.models.Cliente;
import br.com.fiap.sprint3.models.Vaga;
import br.com.fiap.sprint3.repositories.ClienteRepository;
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
@RequestMapping("/clientes")
public class ApiClienteController {
    @Autowired
    private ClienteRepository repository;

    @GetMapping
    @Cacheable("clientes")
    public Page<Cliente> listar(@PageableDefault Pageable pageable){
         return repository.findAll(pageable);

    }

    @PostMapping
    @CacheEvict(value = "clientes", allEntries = true)
    public ResponseEntity<Cliente> create(@RequestBody @Valid Cliente cliente, UriComponentsBuilder uriBuilder) {
        repository.save(cliente);
        URI uri = uriBuilder
                .path("/api/cliente/{id}")
                .buildAndExpand(cliente.getId())
                .toUri();

        return ResponseEntity.created(uri).body(cliente);

    }

    @PutMapping("{id}")
    @CacheEvict(value = "tasks", allEntries = true)
    public ResponseEntity<Cliente> atualizaVaga(
            @PathVariable Long id,
            @RequestBody Cliente clienteAtualizado
    ){

        Optional<Cliente> optional = repository.findById(id);

        if (optional.isEmpty())
            return ResponseEntity.notFound().build();

        Cliente cliente = optional.get();

        cliente.setCpf(clienteAtualizado.getCpf());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setSenha(clienteAtualizado.getSenha());

        repository.save(cliente);


        return ResponseEntity.ok(cliente);

    }

    @DeleteMapping("{id}")
    @CacheEvict(value = "clientes", allEntries = true)
    public ResponseEntity<Cliente> deletar(@PathVariable Long id){
        Optional<Cliente> cliente = repository.findById(id);

        if (cliente.isEmpty())
            return ResponseEntity.notFound().build();

        repository.deleteById(id);
        return ResponseEntity.ok().build();

    }

}
