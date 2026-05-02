package com.oct.octquiz.Model.Categoria.Stat;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatCategoriaService {
    private final StatCategoriaRepository statCategoriaRepository;

    public StatCategoriaService(StatCategoriaRepository statCategoriaRepository) {
        this.statCategoriaRepository = statCategoriaRepository;
    }

    public void save(StatCategoriaEntity statCategoriaEntity) {
        statCategoriaRepository.save(statCategoriaEntity);
    }

    @Transactional
    public void deleteAllByCategoria(CategoriaEntity categoria) {
        statCategoriaRepository.deleteAllByCategoria(categoria);
    }

    public List<String> getStatByCategoria(CategoriaEntity categoria) {
        List<StatCategoriaEntity> stats=statCategoriaRepository.findAllByCategoria(categoria);
        List<String> results=new ArrayList<>();
        for (StatCategoriaEntity stat:stats) {
            results.add(stat.getStat());
        }
        return results;
    }
}
