package com.project.meetsounds.repositories;

import com.project.meetsounds.domain.models.MeGusta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMeGustaRepository extends MongoRepository<MeGusta, String> {

    @Query("{publicacionId:?0}") //Trar los "MeGusta" segun la id de la publicacion *ver clase MeGusta
    List<MeGusta> findMeGustaByIdPublicacion(String idPublicacion);

    @Query("{usuarioAlias:?0}")
    List<MeGusta> findMeGustaByIdAlias(String usuarioAlias);
}
