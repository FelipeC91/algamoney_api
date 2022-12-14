package com.myportifolio.algamoneyapi.controller;

import com.myportifolio.algamoneyapi.evento.RecursoCriadoEvent;
import com.myportifolio.algamoneyapi.model.Categoria;
import com.myportifolio.algamoneyapi.repository.CategoriaRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody @Valid Categoria categoria, HttpServletResponse response) {
        var categoriaSalva = categoriaRepository.save(categoria);

        eventPublisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);

    }
    @GetMapping("/{codigo}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long codigo) {
         var categoriaOptional = categoriaRepository.findById(codigo);

        if (categoriaOptional.isPresent())
            return ResponseEntity.ok(categoriaOptional.get());

        else
            return ResponseEntity.notFound().build();

    }
}
