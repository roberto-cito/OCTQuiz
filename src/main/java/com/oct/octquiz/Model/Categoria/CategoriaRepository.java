package com.oct.octquiz.Model.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity,Integer> {
    @Query("SELECT MAX(id) FROM CategoriaEntity")
    public Integer findMaxID();
}
