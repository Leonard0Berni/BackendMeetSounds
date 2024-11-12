// IGeneroMusicalRepository.java
package com.project.meetsounds.repositories;

import com.project.meetsounds.domain.models.GeneroMusical;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGeneroMusicalRepository extends MongoRepository<GeneroMusical, String> {
}
