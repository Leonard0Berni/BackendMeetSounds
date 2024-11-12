package com.project.meetsounds.repositories;

import com.project.meetsounds.domain.models.About;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAboutRepository extends MongoRepository<About, String> {
    Optional<About> findByUsuarioId(String usuarioId);
}
