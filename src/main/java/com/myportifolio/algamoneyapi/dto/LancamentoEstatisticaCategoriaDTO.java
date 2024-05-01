package com.myportifolio.algamoneyapi.dto;

import com.myportifolio.algamoneyapi.model.Categoria;

import java.math.BigDecimal;

public record LancamentoEstatisticaCategoriaDTO(
        Categoria categoria,
        BigDecimal total
) {
}
