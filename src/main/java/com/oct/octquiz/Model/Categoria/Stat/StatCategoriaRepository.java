package com.oct.octquiz.Model.Categoria.Stat;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatCategoriaRepository extends JpaRepository<StatCategoriaEntity, StatCategoriaID> {
    void deleteAllByCategoria(CategoriaEntity categoria);

    List<StatCategoriaEntity> findAllByCategoria(CategoriaEntity categoria);
}
