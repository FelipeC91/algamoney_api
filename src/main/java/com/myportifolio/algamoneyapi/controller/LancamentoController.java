package com.myportifolio.algamoneyapi.controller;


import com.myportifolio.algamoneyapi.dto.LancamentoEstatisaticaDiaDTO;
import com.myportifolio.algamoneyapi.dto.LancamentoEstatisticaCategoriaDTO;
import com.myportifolio.algamoneyapi.event.RecursoCriadoEvent;
import com.myportifolio.algamoneyapi.model.Lancamento;
import com.myportifolio.algamoneyapi.projection.LancamentoProjection;
import com.myportifolio.algamoneyapi.repository.LancamentoRepository;
import com.myportifolio.algamoneyapi.repository.filter.LancamentoFilterDTO;
import com.myportifolio.algamoneyapi.service.LancamentoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PreAuthorize("hasAuthority( 'PESQUISAR_LANCAMENTO') ") //and #oauth2.hasScope('READ')
    @GetMapping("/estatistica/por-dia")
    public List<LancamentoEstatisaticaDiaDTO> pesquisarPorDia() {
        return this.lancamentoRepository.listarPorDia(LocalDate.now());
    }


    @PreAuthorize("hasAuthority( 'PESQUISAR_LANCAMENTO') ") //and #oauth2.hasScope('READ')
    @GetMapping("/estatistica/por-categoria")
    public List<LancamentoEstatisticaCategoriaDTO> pesquisarPorCategoria() {
        return this.lancamentoRepository.listarPorCategoria(LocalDate.now());
    }

    @PreAuthorize("hasAuthority( 'PESQUISAR_LANCAMENTO') ") //and #oauth2.hasScope('READ')
    @GetMapping
    public Page<Lancamento> listAll(LancamentoFilterDTO lancamentoFilter, Pageable pageable, @AuthenticationPrincipal Jwt jwt ) {
        return lancamentoRepository.findAll(pageable);

    }

    @PreAuthorize("hasAuthority( 'PESQUISAR_LANCAMENTO')")
    @GetMapping(params = "resume")
    public Page<LancamentoProjection> resume(LancamentoFilterDTO lancamentoFilter, Pageable pageable, @AuthenticationPrincipal Jwt jwt ) {
        return lancamentoRepository.resumir(lancamentoFilter, pageable);

    }
    @PreAuthorize("hasAuthority( 'PESQUISAR_LANCAMENTO') ")
    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> buscarPorId(@PathVariable Long codigo){
        var lancamentoOptional = lancamentoRepository.findById(codigo);

        return lancamentoOptional.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PreAuthorize("hasAuthority( 'CADASTRAR_LANCAMENTO') ")
    @PostMapping
    public ResponseEntity<Lancamento> criarLancamento(@RequestBody @Valid  Lancamento lancamento, HttpServletResponse response) {
        var lancamentoOptional = lancamentoService.save(lancamento);

        eventPublisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoOptional.getCodigo() ));

        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoOptional);

    }

    @PreAuthorize("hasAuthority( 'REMOVER_LANCAMENTO') ")
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLancamento(@PathVariable Long codigo) {
        lancamentoRepository.deleteById(codigo);
    }


    @PreAuthorize("hasAuthority( 'CADASTRAR_LANCAMENTO') ")
    @PutMapping("/{codigo}")
    public Lancamento updateLancamento(@RequestParam("codigo") Long codigo,
                                       @RequestBody Lancamento lacamentoEditado) {
        var lancamentoAtualizado = lancamentoService.update(codigo, lacamentoEditado);

          return lancamentoAtualizado;
    }

}