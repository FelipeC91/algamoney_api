package com.myportifolio.algamoneyapi.repository.customQueries;

import com.myportifolio.algamoneyapi.dto.LancamentoEstatisaticaDiaDTO;
import com.myportifolio.algamoneyapi.dto.LancamentoEstatisticaCategoriaDTO;
import com.myportifolio.algamoneyapi.model.Lancamento;
import com.myportifolio.algamoneyapi.projection.LancamentoProjection;
import com.myportifolio.algamoneyapi.repository.filter.LancamentoFilterDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<Lancamento> criteriaQuery;
    private Root<Lancamento> rootEntity;

    @Override
    public Page<Lancamento> filter(LancamentoFilterDTO lancamentoFilter, Pageable pageable) {
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

        return new PageImpl<Lancamento>(typedQuery.getResultList(), pageable, getRowCount(lancamentoFilter));
    }


    private Predicate[] resolveCriteriaFilter(LancamentoFilterDTO lancamentoFilter) {
        var predicates = new ArrayList<Predicate>();

        if (lancamentoFilter == null) {
            return null;
        }

        if (lancamentoFilter.descricao() != null) {
            var searchKey = ("%" + lancamentoFilter.descricao() + "%").toLowerCase();

            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(rootEntity.get("descricao")), searchKey));
        }

        if (lancamentoFilter.dataVencimentoDesde() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(rootEntity.get("dataVencimento"), lancamentoFilter.dataVencimentoDesde()));
        }

        if (lancamentoFilter.dataVencimentoAte() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(rootEntity.get("dataVencimento"), lancamentoFilter.dataVencimentoAte()));

        }

        return predicates.toArray(new Predicate[predicates.size()]);

    }

    private void resolvePagination(TypedQuery<?> typedQuery, Pageable pageable) {
        var actualPage = pageable.getPageNumber();
        var totalPageSize = pageable.getPageSize();
        var firstResultNumber = actualPage * totalPageSize;

        typedQuery.setFirstResult(firstResultNumber);
        typedQuery.setMaxResults(totalPageSize);
    }

    private Long getRowCount(LancamentoFilterDTO lancamentoFilter) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Long.class);
        var root = query.from(Lancamento.class);

        Predicate[] predicates = this.resolveCriteriaFilter(lancamentoFilter);

        if (predicates.length > 0) {
            query.where(predicates)
                    .select(builder.count(root));

        } else {
            query.select(builder.count(root));
        }


        return entityManager.createQuery(query).getSingleResult();
    }


    @Override
    public Page<LancamentoProjection> resumir(LancamentoFilterDTO lancamentoFilter, Pageable pageable) {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LancamentoProjection> criteriaQuery = criteriaBuilder.createQuery(LancamentoProjection.class);
        this.rootEntity = criteriaQuery.from(Lancamento.class);

        criteriaQuery.select(criteriaBuilder.construct(LancamentoProjection.class,
                        rootEntity.get("codigo"),
                        rootEntity.get("descricao"),
                        rootEntity.get("dataVencimento"),
                        rootEntity.get("dataPagamento"),
                        rootEntity.get("valor"),
                        rootEntity.get("tipo"),
                        rootEntity.get("categoria").get("nome"),
                        rootEntity.get("pessoa").get("nome")
                )
        );

        Predicate predicates[] = resolveCriteriaFilter(lancamentoFilter);

        if (predicates.length > 0)
            criteriaQuery.where(predicates);

        var typedQuery = entityManager.createQuery(criteriaQuery);

        resolvePagination(typedQuery, pageable);

        return new PageImpl<LancamentoProjection>(typedQuery.getResultList(), pageable, getRowCount(lancamentoFilter));


    }

    @Override
    public List<LancamentoEstatisticaCategoriaDTO> listarPorCategoria(LocalDate mesReferencia) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisticaCategoriaDTO.class);

        var criteriaFrom = criteriaQuery.from(Lancamento.class);

        criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoriaDTO.class,
                                                        criteriaFrom.get("categoria"),
                                                        criteriaBuilder.sum(criteriaFrom.get("valor"))
                                                        )
                            );

        var primeiroDiaDoMesReferencia = mesReferencia.withDayOfMonth(1);
        var ultimoDiaDoMesReferencia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

        criteriaQuery
                .where(
                    criteriaBuilder.greaterThanOrEqualTo( criteriaFrom.get("dataVencimento"), primeiroDiaDoMesReferencia),
                    criteriaBuilder.lessThanOrEqualTo( criteriaFrom.get("dataVencimento"), ultimoDiaDoMesReferencia)
                )
                .groupBy(criteriaFrom.get("categoria"));

        var typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();

    }

    @Override
    public List<LancamentoEstatisaticaDiaDTO> listarPorDia(LocalDate mesReferencia) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();

        var criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisaticaDiaDTO.class);

        var criteriaFrom = criteriaQuery.from(Lancamento.class);

        criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisaticaDiaDTO.class,
                        criteriaFrom.get("tipo"),
                        criteriaBuilder.sum(criteriaFrom.get("dataVencimento")),
                        criteriaBuilder.sum(criteriaFrom.get("valor"))
                )
        );

        var primeiroDiaDoMesReferencia = mesReferencia.withDayOfMonth(1);
        var ultimoDiaDoMesReferencia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

        criteriaQuery
                .where(
                        criteriaBuilder.greaterThanOrEqualTo( criteriaFrom.get("dataVencimento"), primeiroDiaDoMesReferencia),
                        criteriaBuilder.lessThanOrEqualTo( criteriaFrom.get("dataVencimento"), ultimoDiaDoMesReferencia)
                )
                .groupBy(
                        criteriaFrom.get("tipo"),
                        criteriaFrom.get("dataVencimento")
                );

        var typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }
}
