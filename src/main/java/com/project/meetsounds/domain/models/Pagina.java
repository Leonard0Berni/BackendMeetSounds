package com.project.meetsounds.domain.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Paginas")
public class Pagina {
    private String id;
    private String alias;
    private String nombre;
    private String descripcion;
    private String email;
    private String telefono;
    private Ubicacion ubicacion;
    private Usuario usuario;
}
