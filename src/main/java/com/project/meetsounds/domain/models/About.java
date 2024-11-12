package com.project.meetsounds.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "about")
public class About {
    @Id
    private String id;
    private String rol;
    private List<String> instrumentos;
    private List<String> generoMus;
    private String descripcion;
    private String usuarioId; // Relación con la colección `user`
}
