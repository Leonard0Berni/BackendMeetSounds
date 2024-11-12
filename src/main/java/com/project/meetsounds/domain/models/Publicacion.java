package com.project.meetsounds.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.meetsounds.config.LocalDateTimeDeserializer;
import com.project.meetsounds.config.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Publicaciones")
public class Publicacion {
    @Id
    private String id;
    private String descripcion;
    private String mediaUrl;
    private int count_coment;
    private int count_likes;
    private List<Comentario> comentarios = new ArrayList<>();
    private List<MeGusta> meGustas;
    private String idUsuario;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime fechaPublicacion;

}
