package com.project.meetsounds.repositories;

import com.project.meetsounds.domain.models.TipoInstrumento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITipoInstrumentoRepository extends MongoRepository<TipoInstrumento,String>{

    public TipoInstrumento findByNombre(String nombre);
}
