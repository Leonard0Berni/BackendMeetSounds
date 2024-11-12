package com.project.meetsounds.domain.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Seguidores")
public class Seguidor {
    private String id;
    private Usuario user;
    private Usuario seguidor;
}
