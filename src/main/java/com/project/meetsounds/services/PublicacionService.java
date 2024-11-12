package com.project.meetsounds.services;

import com.project.meetsounds.domain.models.*;
import com.project.meetsounds.repositories.IMeGustaRepository;
import com.project.meetsounds.repositories.IPublicacionRepository;
import com.project.meetsounds.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class PublicacionService {

    @Autowired
    private IPublicacionRepository iPublicacionRepository;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Autowired
    private IMeGustaRepository iMeGustaRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private MongoTemplate mongoTemplate;



    public int contarPublicacionesUsuario(String idAlias){
        Optional<Usuario> usuarioOptional = iUsuarioRepository.findByAlias(idAlias);
        Usuario usu = usuarioOptional.orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el usuario con el alias: " + idAlias));
        int publicaciones = iPublicacionRepository.findAllByIdUsuario(usu.getId()).size();
        return publicaciones;
    }

    public void crearPublicacion(String idAlias, String descripcion, MultipartFile file) {
        Publicacion publi = new Publicacion();

        Optional<Usuario> usuarioOptional = iUsuarioRepository.findByAlias(idAlias);

        Usuario usu = usuarioOptional.orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el usuario con el alias: " + idAlias));

        publi.setDescripcion(descripcion);
        publi.setIdUsuario(usu.getId());
        publi.setFechaPublicacion(LocalDateTime.now());

        // Verificar si el archivo es nulo o vacío antes de intentar acceder a sus propiedades
        if (file != null && !file.isEmpty()) {
            if (!file.getContentType().startsWith("image/")) {
                System.out.println("El archivo debe ser una imagen");
            }
            try {
                // Subir la imagen a S3 (con la lógica de verificación de duplicados)
                String fileUrl = s3Service.uploadFile(file);
                publi.setMediaUrl(fileUrl);
            } catch (IOException | NoSuchAlgorithmException e) {
                System.out.println("Error al subir la imagen");
            }
        }

        Publicacion publicacion = iPublicacionRepository.save(publi);

        List<String> publicacionsUsuario = usu.getMisPublicaciones();
        publicacionsUsuario.add(publicacion.getId());
        usu.setMisPublicaciones(publicacionsUsuario);
        iUsuarioRepository.save(usu);
    }

    public Page<Publicacion> listarMultimediaUsuario(String alias, int page, int size) {
        // Obtener el usuario a partir del alias
        Optional<Usuario> usuarioOptional = iUsuarioRepository.findByAlias(alias);
        Usuario user = usuarioOptional.orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el usuario con el alias: " + alias));

        // Crear el Pageable con paginación y ordenamiento
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaPublicacion"));

        // Consultar las publicaciones del usuario con mediaUrl null, utilizando paginación
        return iPublicacionRepository.findAllByIdUsuarioAndMediaUrlIsNotNull(user.getId(), pageable);
    }

    public Page<Publicacion> listarPosteosUsuario(String alias, int page, int size) {
        // Obtener el usuario a partir del alias
        Optional<Usuario> usuarioOptional = iUsuarioRepository.findByAlias(alias);
        Usuario user = usuarioOptional.orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el usuario con el alias: " + alias));

        // Crear el Pageable con paginación y ordenamiento
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaPublicacion"));

        // Consultar las publicaciones del usuario con mediaUrl null, utilizando paginación
        return iPublicacionRepository.findAllByIdUsuarioAndMediaUrlIsNull(user.getId(), pageable);
    }
    public Page<Publicacion> listarPublicacionesUsuario(String alias, int page, int size) {
        // Obtener el usuario a partir del alias
        Optional<Usuario> usuarioOptional = iUsuarioRepository.findByAlias(alias);
        Usuario user = usuarioOptional.orElseThrow(() -> new IllegalArgumentException("No se ha encontrado el usuario con el alias: " + alias));

        // Crear el Pageable con paginación y ordenamiento
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaPublicacion"));

        // Consultar las publicaciones del usuario con mediaUrl null, utilizando paginación
        return iPublicacionRepository.findAllByIdUsuario(user.getId(), pageable);
    }

    public Page<Publicacion> listarPublicaciones(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaPublicacion"));
        return iPublicacionRepository.findAll(pageable);
    }


    public void eliminarPublicacion(String idAlias, String idPublicacion) {
        // Busca el usuario por alias
        Optional<Usuario> usuarioOptional = iUsuarioRepository.findByAlias(idAlias);
        if (usuarioOptional.isEmpty()) {
            throw new IllegalArgumentException("No se ha encontrado el usuario con alias: " + idAlias);
        }

        Usuario usuario = usuarioOptional.get();

        // Elimina la publicación de las publicaciones del usuario
        List<String> misPublicaciones = usuario.getMisPublicaciones();
        boolean isRemoved = misPublicaciones.removeIf(s -> s.equals(idPublicacion));

        if (!isRemoved) {
            throw new IllegalArgumentException("No se encontró la publicación con ID: " + idPublicacion);
        }

        // Actualiza la lista de publicaciones en la base de datos
        Query queryUp = new Query(Criteria.where("alias").is(idAlias));
        Update update = new Update().set("misPublicaciones", misPublicaciones);
        mongoTemplate.updateFirst(queryUp, update, Usuario.class);

        // Elimina la publicación de la colección de publicaciones
        iPublicacionRepository.deleteById(idPublicacion);
    }


    public Boolean comprobarEsDuenoPublicacion(String idPublicacion, String usuarioAlias){
        Optional<Publicacion> publicacionOptional = iPublicacionRepository.findById(idPublicacion);
        Optional<Usuario> usuarioOptional = iUsuarioRepository.findByAlias(usuarioAlias);

        Publicacion publicacion = publicacionOptional.orElseThrow(() ->
                new IllegalArgumentException("No se ha encontrado la publicacion con id: " + idPublicacion));
        Usuario usuario = usuarioOptional.orElseThrow(() ->
                new IllegalArgumentException("No se ha encontrado el usuario con alias: " + usuarioAlias));

        if(publicacion.getIdUsuario().equals(usuario.getId())){
            return true;
        }else {
            return false;
        }

    }


    public Boolean darMeGusta(String idPublicacion, String usuarioAlias) {
        Optional<Publicacion> publicacionOptional = iPublicacionRepository.findById(idPublicacion);
        Optional<Usuario> usuarioOptional = iUsuarioRepository.findByAlias(usuarioAlias);

        Publicacion publicacion = publicacionOptional.orElseThrow(() ->
                new IllegalArgumentException("No se ha encontrado la publicacion con id: " + idPublicacion));
        Usuario usuario = usuarioOptional.orElseThrow(() ->
                new IllegalArgumentException("No se ha encontrado el usuario con alias: " + usuarioAlias));

        // Inicializar la lista si es null
        List<MeGusta> meGustas = publicacion.getMeGustas();
        if (meGustas == null) {
            meGustas = new ArrayList<>();
            publicacion.setMeGustas(meGustas);  // Actualizar la lista en la publicación
        }

        MeGusta meGusta = new MeGusta();
        meGusta.setPublicacionId(publicacion.getId());
        meGusta.setUsuarioAlias(usuario.getAlias());
        meGustas.add(iMeGustaRepository.save(meGusta));
        publicacion.setCount_likes(publicacion.getCount_likes() + 1);
        // Guardar la publicación actualizada
        iPublicacionRepository.save(publicacion);

        return true;
    }

    public Boolean quitarMeGusta(String idPublicacion, String usuarioAlias) {
        Optional<Publicacion> publicacionOptional = iPublicacionRepository.findById(idPublicacion);
        Optional<Usuario> usuarioOptional = iUsuarioRepository.findByAlias(usuarioAlias);

        Publicacion publicacion = publicacionOptional.orElseThrow(() ->
                new IllegalArgumentException("No se ha encontrado la publicacion con id: " + idPublicacion));
        Usuario usuario = usuarioOptional.orElseThrow(() ->
                new IllegalArgumentException("No se ha encontrado el usuario con alias: " + usuarioAlias));

        // Obtener la lista de "me gustas" y verificar si está vacía
        List<MeGusta> meGustas = publicacion.getMeGustas();
        if (meGustas == null || meGustas.isEmpty()) {
            throw new IllegalArgumentException("No existen 'me gustas' en esta publicación para eliminar");
        }

        // Buscar el "me gusta" del usuario
        MeGusta meGustaToRemove = meGustas.stream()
                .filter(meGusta -> meGusta.getUsuarioAlias().equals(usuario.getAlias()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El usuario no ha dado 'me gusta' a esta publicación"));

        // Eliminar el "me gusta" y reducir el contador
        meGustas.remove(meGustaToRemove);
        iMeGustaRepository.delete(meGustaToRemove);

        // Disminuir el contador de "likes" si es mayor a 0
        if (publicacion.getCount_likes() > 0) {
            publicacion.setCount_likes(publicacion.getCount_likes() - 1);
        }

        // Guardar la publicación actualizada
        iPublicacionRepository.save(publicacion);

        return true;
    }

    public Boolean usuarioHaDadoMeGusta(String idPublicacion, String usuarioAlias) {
        // Buscar la publicación
        Optional<Publicacion> publicacionOptional = iPublicacionRepository.findById(idPublicacion);
        Publicacion publicacion = publicacionOptional.orElseThrow(() ->
                new IllegalArgumentException("No se ha encontrado la publicacion con id: " + idPublicacion));

        // Obtener la lista de "me gustas" de la publicación
        List<MeGusta> meGustas = publicacion.getMeGustas();

        // Verificar si la lista de "me gustas" contiene uno del usuario
        if (meGustas != null) {
            return meGustas.stream()
                    .anyMatch(meGusta -> meGusta.getUsuarioAlias().equals(usuarioAlias));
        }

        return false;  // Retorna false si la lista es null o el usuario no ha dado "me gusta"
    }





    public Publicacion buscarPublicacionPorId(String id) {
        Optional<Publicacion> publicacionOptional = iPublicacionRepository.findById(id);
        Publicacion publicacion = publicacionOptional.orElseThrow(()-> new IllegalArgumentException("No se ha encontrado la publicacion con id: " + id));
        return publicacion;
    }


}
