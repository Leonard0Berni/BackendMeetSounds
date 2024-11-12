package com.project.meetsounds.domain.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Chats")
public class Chat {
    @Id
    private String id;
    private String idUsuario1;
    private String idUsuario2;
    private List<Mensaje> mensajes = new ArrayList<>();
}
