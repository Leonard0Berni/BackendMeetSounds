package com.project.meetsounds.repositories;

import com.project.meetsounds.domain.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IChatRepository extends MongoRepository<Chat, String> {

    Optional<Chat> findByIdUsuario1AndIdUsuario2(String idUsuario1, String idUsuario2);
    Optional<Chat> findByIdUsuario2AndIdUsuario1(String idUsuario1, String idUsuario2);
    List<Chat> findByIdUsuario1OrIdUsuario2(String idUsuario);
}
