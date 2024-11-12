package com.project.meetsounds.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Bandas")
public class Banda {
    @Id
    private String id;
    private String urlFotoBanda;
    private String nombreBanda;
    private String descripcion;
    private String idCreador;
    private List<String> miembros = new ArrayList<>();
    private List<String>  galeria = new ArrayList<>();
    private List<String> seguidores = new ArrayList<>();
}
