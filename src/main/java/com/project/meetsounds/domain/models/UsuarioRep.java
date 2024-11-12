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
@Document(collection = "UsuarioReportado")
public class UsuarioRep {
    private String id;
    private Usuario usuario; //Este es el que pone la denuncia.
    private Usuario denunciado;
    private Motivo motivo;
}
