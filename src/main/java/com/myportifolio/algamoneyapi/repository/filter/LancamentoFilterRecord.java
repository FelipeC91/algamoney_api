package com.myportifolio.algamoneyapi.repository.filter;

import java.time.LocalDate;

public record LancamentoFilterRecord(
        String descricao, LocalDate dataVencimentoDesde, LocalDate dataVencimentoAte) {
}
