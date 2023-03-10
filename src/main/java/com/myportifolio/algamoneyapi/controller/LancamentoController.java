package com.myportifolio.algamoneyapi.controller;


import com.myportifolio.algamoneyapi.evento.RecursoCriadoEvent;
import com.myportifolio.algamoneyapi.model.Lancamento;
import com.myportifolio.algamoneyapi.repository.LancamentoRepository;
import com.myportifolio.algamoneyapi.repository.filter.LancamentoFilterRecord;
import com.myportifolio.algamoneyapi.service.LancamentoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lancamento")
public class LancamentoController {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping
    public Page<Lancamento> listAll(LancamentoFilterRecord lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.filter(lancamentoFilter, pageable);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> buscarPorId(@PathVariable Long codigo){
        var lancamentoOptional = lancamentoRepository.findById(codigo);

        if (lancamentoOptional.isPresent())
            return ResponseEntity.ok(lancamentoOptional.get());
        else
            return ResponseEntity.notFound().build();

    }

    @PostMapping
    public ResponseEntity<Lancamento> criarLancamento(@RequestBody @Valid  Lancamento lancamento, HttpServletResponse response) {
        var lancamentoOptional = lancamentoService.save(lancamento);

        eventPublisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoOptional.getCodigo() ));

        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoOptional);

    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLancamento(@PathVariable Long codigo) {
        lancamentoRepository.deleteById(codigo);
    }



}