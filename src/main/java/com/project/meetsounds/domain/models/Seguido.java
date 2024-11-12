package com.project.meetsounds.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Seguidos")
public class Seguido {
    private String id;
    private Usuario user;
    private List<String> misSeguidos;
}

