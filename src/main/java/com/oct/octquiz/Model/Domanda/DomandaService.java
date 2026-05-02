package com.oct.octquiz.Model.Domanda;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@Transactional
public class DomandaService {
    private final DomandaRepository domandaRepository;
    public DomandaService(DomandaRepository domandaRepository) {
        this.domandaRepository = domandaRepository;
    }

    @Transactional
    public void save(DomandaEntity domandaEntity) {
        Integer maxId=null;
        try {
            maxId=domandaRepository.findMaxIdByCategoria(domandaEntity.getCategoria());
        } catch (Exception ignored) {}
        if(maxId==null) maxId=1;
        else maxId++;
        domandaEntity.setId(maxId);
        domandaRepository.save(domandaEntity);
        domandaRepository.flush();
    }

    public void delete(DomandaEntity domandaEntity) throws IOException {
        int idDeleted = domandaEntity.getId();
        CategoriaEntity categoria = domandaEntity.getCategoria();
        String foto = domandaEntity.getFoto();

        domandaRepository.delete(domandaEntity);
        domandaRepository.flush();

        if(foto!=null && !foto.isEmpty()) {
            Path path = Path.of(foto);
            if (Files.exists(path)) {
                Files.delete(path);
            }
        }

        domandaRepository.decreaseIdsAbove(categoria, idDeleted);
    }

    public void deleteAll(CategoriaEntity categoria) throws IOException {
        List<DomandaEntity> domande=domandaRepository.findAllByCategoria(categoria);
        for(DomandaEntity d:domande) {
            if(d.getFoto() != null && !d.getFoto().isEmpty()) {
                Path path = Path.of(d.getFoto());
                if (Files.exists(path)) {
                    Files.delete(path);
                }
            }
        }
        domandaRepository.deleteAll(domande);
        domandaRepository.flush();
    }

    public List<DomandaEntity> findAllByCategoria(CategoriaEntity categoria) {
        return domandaRepository.findAllByCategoria(categoria);
    }

    public DomandaEntity findByCategoriaAndId(CategoriaEntity categoria, Integer idDomanda) {
        return domandaRepository.findByCategoriaAndId(categoria,idDomanda).orElse(null);
    }

    @Transactional
    public void update(DomandaEntity domandaEntity) {
        domandaRepository.save(domandaEntity);
    }
}
