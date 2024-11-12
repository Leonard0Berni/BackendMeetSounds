package com.project.meetsounds.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.meetsounds.domain.models.Seguido;

@Repository
public interface ISeguirRepository extends MongoRepository<Seguido, String>{

    @Query("{user:?0}")
    Seguido findByidUsuario(String idUsuario);

}
