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
@Document(collection = "Ubicaciones")
public class Ubicacion {
    private String id;
    private Pais pais;
    private Estado estado;
    private Departamento departamento;
    private String urlMapa;
}
