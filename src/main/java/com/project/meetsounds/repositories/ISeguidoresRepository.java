package com.project.meetsounds.repositories;

import com.project.meetsounds.domain.models.Seguidor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISeguidoresRepository extends MongoRepository<Seguidor, String> {
}
