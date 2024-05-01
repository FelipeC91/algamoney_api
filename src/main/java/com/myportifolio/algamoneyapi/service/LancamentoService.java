package com.myportifolio.algamoneyapi.service;

import com.myportifolio.algamoneyapi.model.Lancamento;
import com.myportifolio.algamoneyapi.repository.LancamentoRepository;
import com.myportifolio.algamoneyapi.repository.PessoaRepository;
import com.myportifolio.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LancamentoService {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Transactional
    public Lancamento save(Lancamento lancamento) {
        validatePessoa(lancamento);

        return lancamentoRepository.save(lancamento);

    }

    private void validatePessoa(Lancamento lancamento) {
        var pessoaCodigo = lancamento.getPessoa().getCodigo();
        var pessoaOptional = pessoaRepository.findById(pessoaCodigo);

        if (pessoaOptional.isEmpty() || pessoaOptional.get().isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }
    }

    public Lancamento update(Long codigo, Lancamento lancamentoEditado) {
        var lancamentoValido = lancamentoRepository.findById(codigo)
                                .orElseThrow(IllegalMonitorStateException::new);

        if (lancamentoValido.equals(lancamentoEditado))
            validatePessoa(lancamentoEditado);

        BeanUtils.copyProperties(lancamentoValido, lancamentoEditado, "codigo");

        return lancamentoRepository.save(lancamentoValido);
    }
}
