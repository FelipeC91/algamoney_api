package com.myportifolio.algamoneyapi.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoLancamento {
    DESPESA("Receita"),
    RECEITA("Despesa");

    private String descricao;

}
