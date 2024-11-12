package com.project.meetsounds.repositories;

import com.project.meetsounds.domain.models.Instrumento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IInstrumentoRepository extends MongoRepository<Instrumento,String>{

    
    public List<Instrumento> findByTipoInstrumento(String tipo);
    public Optional<Instrumento> findByNombre(String nombre);
}
