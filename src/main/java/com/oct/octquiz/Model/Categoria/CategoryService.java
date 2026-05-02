package com.oct.octquiz.Model.Categoria;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CategoryService {
    private final CategoriaRepository categoriaRepository;

    public CategoryService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public void save(CategoriaEntity categoriaEntity) {
        Integer maxId= categoriaRepository.findMaxID();
        if(maxId==null) maxId=1;
        else maxId++;
        categoriaEntity.setId(maxId);
        categoriaRepository.save(categoriaEntity);
        categoriaRepository.flush();
    }

    public CategoriaEntity findById(int id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public List<CategoriaEntity> findAll() {
        return categoriaRepository.findAll();
    }

    @Transactional
    public void deleteById(int id) {
        categoriaRepository.deleteById(id);
        categoriaRepository.flush();
    }

    @Transactional
    public void update(CategoriaEntity categoriaEntity) {
        categoriaRepository.save(categoriaEntity);
    }
}
