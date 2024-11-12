package com.project.meetsounds.domain.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class Usuario {
    private String id;
    private String fotoPerfilUrl;
    private String fotoPortadaUrl;
    private String nombre;
    private String apellido;
    private String nombreUsuario;
    private String alias;
    private String contrasena; //A veces no reconoce la Ñ
    private String email;
    private String telefono;
    private int edad;
    private String genero;
    private LocalDate fechaNacimiento;
    private Ubicacion ubicacion;
    private int c_seguidores;
    private int c_seguidos;
    private List<String> seguidos = new ArrayList<>();
    private List<String> seguidores = new ArrayList<>();

    private List<Instrumento> misInstru = new ArrayList<>();
    
    private List<Roles> roles = new ArrayList<>();
    private List<Banda> misBandas = new ArrayList<>();
    private List<String> misPublicaciones = new ArrayList<>();

    private String descripcion;
    private Redes redes;
    private LocalDate date;

    private String rol;
    private List<GeneroMusical> misGeneros = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id); // Asegúrate de usar un campo único
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Asegúrate de usar un campo único
    }
}
