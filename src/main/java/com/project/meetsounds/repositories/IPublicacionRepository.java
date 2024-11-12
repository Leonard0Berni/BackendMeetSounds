package com.project.meetsounds.repositories;

import com.project.meetsounds.domain.models.Publicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPublicacionRepository extends MongoRepository<Publicacion, String> {

    Page<Publicacion> findAllByIdUsuarioAndMediaUrlIsNull(String idUsuario, Pageable pageable);
    Page<Publicacion> findAllByIdUsuarioAndMediaUrlIsNotNull(String idUsuario, Pageable pageable);
    Page<Publicacion> findAllByIdUsuario(String idUsuario, Pageable pageable);
    List<Publicacion> findAllByIdUsuario(String idUsuario);
}
