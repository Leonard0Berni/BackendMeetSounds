package com.project.meetsounds.domain.models;

import lombok.*;

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
public class PublicacionOut {
    private String id;
    private String descripcion;
    private String mediaUrl;
    private int count_coment;
    private int count_likes;
    private List<ComentarioOut> comentariosOut = new ArrayList<>();
    private Usuario usuario;
}
