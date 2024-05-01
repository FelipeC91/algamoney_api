package com.myportifolio.algamoneyapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Lancamento {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Size(max = 100)
    private String descricao;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;


    @NotNull
    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @NotNull
    private BigDecimal valor;

    @Size(max = 200)
    private String observacao;


    @Enumerated(EnumType.STRING)
    private TipoLancamento tipo;

    @ManyToOne
    @JoinColumn(name = "codigo_categoria")
    @NotNull
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "codigo_pessoa")
    @NotNull
    private Pessoa pessoa;



}
