package com.myportifolio.algamoneyapi.controller;

import com.myportifolio.algamoneyapi.event.RecursoCriadoEvent;
import com.myportifolio.algamoneyapi.model.Pessoa;
import com.myportifolio.algamoneyapi.repository.PessoaRepository;
import com.myportifolio.algamoneyapi.service.PessoaService;
import com.myportifolio.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PreAuthorize("hasAuthority( 'PESQUISAR_CATEGORIA') ")
    @GetMapping
    public List<Pessoa> listarPessoas() {
        return pessoaRepository.findAll();


    }
    @PreAuthorize("hasAuthority( 'PESQUISAR_CATEGORIA') ")
    @GetMapping
    public Pessoa pesquisarPorPessoas(@RequestParam("nome") String nome) {
        return pessoaRepository.findByNome(nome)
                .orElseThrow(PessoaInexistenteOuInativaException::new);

    }

    @PreAuthorize("hasAuthority( 'CADASTRAR_PESSOA') ")
    @PostMapping()
    public ResponseEntity<Pessoa> criarPessoa(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
        var pessoaSalva = pessoaRepository.save(pessoa);

        eventPublisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @PreAuthorize("hasAuthority( 'REMOVER_PESSOA') ")
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePessoa(@PathVariable("codigo") Long pessoaCodigo, HttpServletResponse response) {
        pessoaRepository.deleteById(pessoaCodigo);

    }

    @PreAuthorize("hasAuthority( 'CADASTRAR_PESSOA') ")
    @PutMapping("/{codigo}")
    public ResponseEntity<Pessoa> updatePessoa(@PathVariable("codigo") Long pessoaCodigo, @Valid @RequestBody Pessoa pessoaSource) {
        var pessoaUpdated = pessoaService.update(pessoaCodigo, pessoaSource);

        return ResponseEntity.ok(pessoaUpdated);
    }

    @PreAuthorize("hasAuthority( 'CADASTRAR_PESSOA') ")
    @PutMapping(value = "/{codigo}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAtivoProperty(@PathVariable("codigo") Long pessoaCodigo, @RequestBody Boolean ativo) {
        pessoaService.proertieAtivoUpdate(pessoaCodigo, ativo);
    }
}
