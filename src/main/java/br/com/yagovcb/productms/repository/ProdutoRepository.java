package br.com.yagovcb.productms.repository;

import br.com.yagovcb.productms.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 *  Classe Repository da entidade {@link Produto}
 *
 *  Criado por Yago Castelo Branco
 *
 * @since 21/06/2021
 * */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, String> {

    @Modifying
    @Query("SELECT new br.com.yagovcb.productms.model.Produto( p.id, p.name, p.description, p.price ) FROM Produto p " +
            "WHERE (p.price >= :minPrice or :minPrice IS NULL) " +
            "AND (p.price <= :maxPrice or :maxPrice IS NULL) " +
            "AND (:q IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Produto> listarProdutosPorFiltro(String q, Double minPrice, Double maxPrice);

    @Query(name = "Produtos.findById", nativeQuery = true)
    @RestResource(exported = false)
    Optional<Produto> findById (@Param(value = "id") String id);

    Page<Produto> findAll (Pageable pageable);

    Produto findByNameOrDescription(@Param(value = "name") String name, @Param(value = "description") String description);
}
