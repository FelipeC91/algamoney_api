package com.myportifolio.algamoneyapi.controller;

import com.myportifolio.algamoneyapi.evento.RecursoCriadoEvent;
import com.myportifolio.algamoneyapi.model.Pessoa;
import com.myportifolio.algamoneyapi.repository.PessoaRepository;
import com.myportifolio.algamoneyapi.service.PessoaService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping
    public List<Pessoa> listarPessoas() {
        return pessoaRepository.findAll();
    }
    @PostMapping()
    public ResponseEntity<Pessoa> criarPessoa(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
        var pessoaSalva = pessoaRepository.save(pessoa);

        eventPublisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePessoa(@PathVariable("codigo") Long pessoaCodigo, HttpServletResponse response) {
        pessoaRepository.deleteById(pessoaCodigo);

    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Pessoa> updatePessoa(@PathVariable("codigo") Long pessoaCodigo, @Valid @RequestBody Pessoa pessoaSource) {
        var pessoaUpdated = pessoaService.update(pessoaCodigo, pessoaSource);

        return ResponseEntity.ok(pessoaUpdated);
    }

    @PutMapping("/{codigo}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAtivoPropertie(@PathVariable("codigo") Long pessoaCodigo, @RequestBody Boolean ativo) {
        pessoaService.proertieAtivoUpdate(pessoaCodigo, ativo);
    }
}
