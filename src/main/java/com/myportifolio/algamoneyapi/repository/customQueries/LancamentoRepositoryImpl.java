package com.myportifolio.algamoneyapi.repository.customQueries;

import com.myportifolio.algamoneyapi.model.Lancamento;
import com.myportifolio.algamoneyapi.repository.filter.LancamentoFilterRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQueries {

   @PersistenceContext
    private EntityManager entityManager;

   private CriteriaBuilder criteriaBuilder;
   private CriteriaQuery<Lancamento> criteriaQuery;
   private Root<Lancamento> rootEntity;

    @Override
    public List<Lancamento> filter(LancamentoFilterRecord lancamentoFilter) {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.criteriaQuery = criteriaBuilder.createQuery(Lancamento.class);
        this.rootEntity = criteriaQuery.from(Lancamento.class);

        Predicate predicates[] = resolveCriteriaFilter(lancamentoFilter);

        if (predicates.length > 0)
            criteriaQuery.where(predicates);
        else
            criteriaQuery.select(rootEntity);


        return  entityManager.createQuery(criteriaQuery).getResultList();
    }

    private Predicate[] resolveCriteriaFilter(LancamentoFilterRecord lancamentoFilter) {
        var predicates = new ArrayList<Predicate>();

        if (lancamentoFilter == null) {
            return null;
        }

        if (lancamentoFilter.descricao() != null) {
            var searchKey = ("%" +lancamentoFilter.descricao() + "%").toLowerCase();

            predicates.add( criteriaBuilder.like(  criteriaBuilder.lower(rootEntity.get("descricao") ),searchKey) );
        }

        if (lancamentoFilter.dataVencimentoDesde() != null) {
            System.out.println("desde "+lancamentoFilter.dataVencimentoDesde());
            predicates.add( criteriaBuilder.greaterThanOrEqualTo(rootEntity.get("dataVencimento"), lancamentoFilter.dataVencimentoDesde()));
        }

        if (lancamentoFilter.dataVencimentoAte() != null) {
            System.out.println("ate "+lancamentoFilter.dataVencimentoAte());
            predicates.add( criteriaBuilder.lessThanOrEqualTo(rootEntity.get("dataVencimento"), lancamentoFilter.dataVencimentoAte()));

        }

        return predicates.toArray(new Predicate[predicates.size()]);

    }
}
