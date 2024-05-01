package com.myportifolio.algamoneyapi.repository.filter;

import java.time.LocalDate;

public record LancamentoFilterDTO(
        String descricao,
        LocalDate dataVencimentoDesde,
        LocalDate dataVencimentoAte
) {}
