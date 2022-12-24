package com.myportifolio.algamoneyapi.service;

import com.myportifolio.algamoneyapi.model.Pessoa;
import com.myportifolio.algamoneyapi.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    @Transactional
    public Pessoa update(Long pessoaCodigo, Pessoa pessoaSource) {
            var pessoaTarget = findPessoaById(pessoaCodigo);

            BeanUtils.copyProperties(pessoaSource, pessoaTarget, "codigo");

            return pessoaRepository.save(pessoaTarget);

    }

    @Transactional
    public void proertieAtivoUpdate(Long pessoaCodigo, Boolean ativo) {
        var pessoaTarget = findPessoaById(pessoaCodigo);
        pessoaTarget.setAtivo(ativo);

        pessoaRepository.save(pessoaTarget);
    }

    public Pessoa findPessoaById(Long codigoPessoa) {
        var pessoaOptional = pessoaRepository.findById(codigoPessoa);

        if (pessoaOptional.isEmpty())
            throw new EmptyResultDataAccessException(1);

        return pessoaOptional.get();
    }
}
