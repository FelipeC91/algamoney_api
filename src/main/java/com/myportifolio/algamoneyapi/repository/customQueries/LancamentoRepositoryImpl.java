package com.myportifolio.algamoneyapi.repository.customQueries;

import com.myportifolio.algamoneyapi.model.Lancamento;
import com.myportifolio.algamoneyapi.repository.filter.LancamentoFilterRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Predicates;

import java.util.ArrayList;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQueries {

   @PersistenceContext
    private EntityManager entityManager;

   private CriteriaBuilder criteriaBuilder;
   private CriteriaQuery<Lancamento> criteriaQuery;
   private Root<Lancamento> rootEntity;

    @Override
    public Page<Lancamento> filter(LancamentoFilterRecord lancamentoFilter, Pageable pageable) {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.criteriaQuery = criteriaBuilder.createQuery(Lancamento.class);
        this.rootEntity = criteriaQuery.from(Lancamento.class);

        Predicate predicates[] = resolveCriteriaFilter(lancamentoFilter);

        if (predicates.length > 0) {
            criteriaQuery.where(predicates);
        } else
            criteriaQuery.select(rootEntity);

        var typedQuery = entityManager.createQuery(criteriaQuery);

        resolvePagination(typedQuery, pageable);

        return  new PageImpl<Lancamento>(typedQuery.getResultList(), pageable, getRowCount(lancamentoFilter));
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
            predicates.add( criteriaBuilder.greaterThanOrEqualTo(rootEntity.get("dataVencimento"), lancamentoFilter.dataVencimentoDesde()));
        }

        if (lancamentoFilter.dataVencimentoAte() != null) {
            predicates.add( criteriaBuilder.lessThanOrEqualTo(rootEntity.get("dataVencimento"), lancamentoFilter.dataVencimentoAte()));

        }

        return predicates.toArray(new Predicate[predicates.size()]);

    }

    private void resolvePagination(TypedQuery<Lancamento> typedQuery, Pageable pageable) {
        var actualPage =  pageable.getPageNumber();
        var totalPageSize = pageable.getPageSize();
        var firstResultNumber = actualPage * totalPageSize;

        typedQuery.setFirstResult(firstResultNumber);
        typedQuery.setMaxResults(totalPageSize);
    }

    private Long getRowCount(LancamentoFilterRecord lancamentoFilter) {
        var builder = entityManager.getCriteriaBuilder();
        var query  = builder.createQuery(Long.class);
        var root = query.from(Lancamento.class);

        Predicate[] predicates = this.resolveCriteriaFilter(lancamentoFilter);

        if (predicates.length > 0) {
            query.where(predicates)
                    .select( builder.count(root));

        }else {
            query.select( builder.count(root));
        }


        return entityManager.createQuery(query).getSingleResult();
    }
}
