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
@Document(collection = "PublicacionReportada")
public class PublicacionRep {
    private String id;
    private Usuario usuario;
    private Motivo motivo;
}

