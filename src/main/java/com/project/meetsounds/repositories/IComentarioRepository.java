package com.project.meetsounds.repositories;

import com.project.meetsounds.domain.models.Comentario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IComentarioRepository extends MongoRepository<Comentario, String> {
}
