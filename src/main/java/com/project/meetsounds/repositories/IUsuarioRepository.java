package com.project.meetsounds.repositories;

import com.project.meetsounds.domain.models.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends MongoRepository<Usuario, String> {

    @Query("{ 'nombre': { $regex: ?0, $options: 'i' } }")
    List<Usuario> findByNombre(String text);

    @Query("{ 'apellido': { $regex: ?0, $options: 'i' } }") // Cambia 'nombre' a 'apellido'
    List<Usuario> findByApellido(String text);

    @Query("{ 'alias': { $regex: ?0, $options: 'i' } }") // Cambia 'nombre' a 'alias'
    List<Usuario> findByAliasBrrBusqueda(String alias);

    @Query("{ 'alias' : ?0 }")
    Optional<Usuario> findByAlias(String alias);





    Optional<Usuario> findByEmail(String email);

    @Query("{ 'alias': { $in: ?0 } }")
    List<Usuario> findAllByAlias(List<String> usuarioAliasList);


    boolean existsByAlias(String alias);
}
