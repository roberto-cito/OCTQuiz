package com.oct.octquiz.Model.Domanda;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DomandaRepository extends JpaRepository<DomandaEntity,DomandaID> {
    public List<DomandaEntity> findAllByCategoria(CategoriaEntity categoria);

    @Query("SELECT MAX(d.id) FROM DomandaEntity d WHERE d.categoria = :categoria")
    public Integer findMaxIdByCategoria(@Param("categoria") CategoriaEntity categoria);

    public Optional<DomandaEntity> findByCategoriaAndId(CategoriaEntity categoria, int id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE DomandaEntity d SET d.id = d.id - 1 WHERE d.categoria = :categoria AND d.id > :id")
    void decreaseIdsAbove(@Param("categoria") CategoriaEntity categoria, @Param("id") int id);
}
