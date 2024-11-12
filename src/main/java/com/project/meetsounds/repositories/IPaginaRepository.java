package com.project.meetsounds.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.meetsounds.domain.models.Pagina;

@Repository
public interface IPaginaRepository extends MongoRepository<Pagina, String>{

}
