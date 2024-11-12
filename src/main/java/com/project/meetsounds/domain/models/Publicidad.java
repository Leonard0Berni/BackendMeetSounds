package com.project.meetsounds.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Publicidades")
public class Publicidad {
    private String id;
    private String descripcion;
    private String media;
    private int count_coment;
    private List<Comentario> comentarios;
    private int count_meGusta;
    private List<MeGusta> meGustas;
    private List<Hashtags> hashtags;
    private Usuario usuario;
    //private Factura factura;
}
