package com.project.meetsounds.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.meetsounds.controlErrores.AliasAlreadyExistsException;
import com.project.meetsounds.controlErrores.AliasAndEmailAlreadyExistsException;
import com.project.meetsounds.controlErrores.EmailAlreadyExistsException;
import com.project.meetsounds.controlErrores.MenorDeEdadException;
import com.project.meetsounds.domain.models.Banda;
import com.project.meetsounds.domain.models.GeneroMusical;
import com.project.meetsounds.domain.models.Instrumento;
import com.project.meetsounds.domain.models.MeGusta;
import com.project.meetsounds.domain.models.Publicacion;
import com.project.meetsounds.domain.models.Redes;
import com.project.meetsounds.domain.models.Usuario;
import com.project.meetsounds.repositories.IAboutRepository;
import com.project.meetsounds.repositories.IMeGustaRepository;
import com.project.meetsounds.repositories.IPublicacionRepository;
import com.project.meetsounds.repositories.IUsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private IMeGustaRepository iMeGustaRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private InstrumentoService instrumentoService;
    @Autowired
    private IPublicacionRepository iPublicacionRepository;
    @Autowired
    S3Service s3Service;
    @Autowired
    private IAboutRepository aboutRepository;
    @Autowired
    private GeneroMusicalService generoMusicalService;

    /*--------------------------------REGISTRO----------------------------------------*/
    public void comprobarCredenciales(Usuario user){
        if (usuarioRepository.findByAlias(user.getAlias()).isPresent() && usuarioRepository.findByEmail(user.getEmail()).isPresent()){ //Si no se encuentra ningun usuario con el mismo alias, el usuario se crea.
            throw new AliasAndEmailAlreadyExistsException("El Alias y el Email ya existen!");
        }

        if(usuarioRepository.findByAlias(user.getAlias()).isPresent()){
            throw new AliasAlreadyExistsException("El Alias ya existe!");
        }

        if(usuarioRepository.findByEmail(user.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("El Email ya existe!");
        }
    }

    public Usuario guardarUsuario(Usuario user) {

        user.setC_seguidores(0);
        user.setC_seguidos(0);
        //Generar Fecha
        LocalDate fechaActual = LocalDate.now();
        int year = fechaActual.getYear();
        int mes = fechaActual.getMonthValue();
        int dia = fechaActual.getDayOfMonth();
        user.setDate(LocalDate.of(year, mes, dia));
        user.setAlias(user.getAlias());

        if(!esMayorDeEdad(user.getFechaNacimiento())){
            throw new MenorDeEdadException("Debe ser mayor de 18 años");
        }

        if (usuarioRepository.findByAlias(user.getAlias()).isPresent() && usuarioRepository.findByEmail(user.getEmail()).isPresent()){ //Si no se encuentra ningun usuario con el mismo alias, el usuario se crea.
            throw new AliasAndEmailAlreadyExistsException("El Alias y el Email ya existen!");
        }

        if(usuarioRepository.findByAlias(user.getAlias()).isPresent()){
            throw new AliasAlreadyExistsException("El Alias ya existe!");
        }

        if(usuarioRepository.findByEmail(user.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("El Email ya existe!");
        }
        return usuarioRepository.save(user);
    }

    


    private boolean esMayorDeEdad(LocalDate fechaNacimiento){
        LocalDate hoy = LocalDate.now();
        Period periodo = Period.between(fechaNacimiento, hoy);
        return periodo.getYears() >= 18;
    }
    /*--------------------------------LOGIN----------------------------------------*/
    public boolean loginUsuario(String username, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByAlias(username);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Aquí puedes implementar un hash o algún algoritmo de seguridad para comparar las contraseñas
            return usuario.getContrasena().equals(contrasena);
        }

        return false;
    }
    /*--------------------------------FOTO DE PERFIL Y DE PORTADA----------------------------------------*/

    public ResponseEntity<String> actualizarFotoPerfilUsuario(MultipartFile file, String alias) {
        // Validaciones previas (opcional), como el tamaño o el tipo de archivo
        Optional<Usuario> usuarioOptional = usuarioRepository.findByAlias(alias);

        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.badRequest().body("No existe usuario con el alias: " + alias);
        }

        Usuario usuario = usuarioOptional.get();

        // Validar archivo (opcional)
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }

        if (!file.getContentType().startsWith("image/")) {
            return ResponseEntity.badRequest().body("El archivo debe ser una imagen");
        }

        try {
            // Subir la imagen a S3 (con la lógica de verificación de duplicados)
            String fileUrl = s3Service.uploadFile(file);

            // Actualizar la URL de la foto de perfil del usuario
            usuario.setFotoPerfilUrl(fileUrl);
            usuarioRepository.save(usuario);

            // Devolver la URL de la imagen (ya sea subida o existente)
            return ResponseEntity.ok("Foto de perfil actualizada exitosamente");

        } catch (IOException | NoSuchAlgorithmException e) {
            return ResponseEntity.status(500).body("Error al subir la imagen: " + e.getMessage());
        }
    }

    public ResponseEntity<String> actualizarFotoPortada(MultipartFile file, String alias) {
        // Validaciones previas (opcional), como el tamaño o el tipo de archivo
        Optional<Usuario> usuarioOptional = usuarioRepository.findByAlias(alias);

        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.badRequest().body("No existe usuario con el alias: " + alias);
        }

        Usuario usuario = usuarioOptional.get();

        // Validar archivo (opcional)
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }

        if (!file.getContentType().startsWith("image/")) {
            return ResponseEntity.badRequest().body("El archivo debe ser una imagen");
        }

        try {
            // Subir la imagen a S3 (con la lógica de verificación de duplicados)
            String fileUrl = s3Service.uploadFile(file);

            // Actualizar la URL de la foto de perfil del usuario
            usuario.setFotoPortadaUrl(fileUrl);
            usuarioRepository.save(usuario);

            // Devolver la URL de la imagen (ya sea subida o existente)
            return ResponseEntity.ok("Foto de portada actualizada exitosamente");

        } catch (IOException | NoSuchAlgorithmException e) {
            return ResponseEntity.status(500).body("Error al subir la imagen: " + e.getMessage());
        }
    }


    /*--------------------------------BUSQUEDA DE USUARIO---------------------------------------*/

    public Optional<Usuario> buscarUsuarioPorId(String id) {
        return this.usuarioRepository.findById(id);
    }

    public List<Usuario> buscarTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Set<Usuario> buscarUsuarioPorTexto(String text) {

        if (text == null || text.isEmpty()) {
            return Collections.emptySet(); // O puedes lanzar una excepción según tu caso
        }
        Set<Usuario> usuarios = new HashSet<>();
        usuarios.addAll(usuarioRepository.findByNombre(text));
        usuarios.addAll(usuarioRepository.findByApellido(text));
        usuarios.addAll(usuarioRepository.findByAliasBrrBusqueda(text));
        return usuarios.stream().limit(5).collect(Collectors.toSet());
    }

    /*--------------------------------SEGUIR Y DEJAR DE SEGUIR----------------------------------------*/

    public void seguirUsuario(String aliasSeguidor, String aliasSeguido) {
        // Asegurarse de que el usuario no se siga a sí mismo
        if (aliasSeguidor.equals(aliasSeguido)) {
            throw new IllegalArgumentException("Un usuario no puede seguirse a sí mismo");
        }

        // Obtener el seguidor y el seguido
        Optional<Usuario> optionalSeguidor = usuarioRepository.findByAlias(aliasSeguidor);
        Optional<Usuario> optionalSeguido = usuarioRepository.findByAlias(aliasSeguido);

        if (optionalSeguidor.isPresent() && optionalSeguido.isPresent()) {
            Usuario usuarioSeguidor = optionalSeguidor.get();
            Usuario usuarioSeguido = optionalSeguido.get();

            // Evitar duplicados y seguir a sí mismo
            if (!usuarioSeguidor.getSeguidos().contains(aliasSeguido)) {
                usuarioSeguidor.getSeguidos().add(aliasSeguido);
                usuarioSeguidor.setC_seguidos(usuarioSeguidor.getC_seguidos() + 1);  // Incrementar c_seguidos
            }

            if (!usuarioSeguido.getSeguidores().contains(aliasSeguidor)) {
                usuarioSeguido.getSeguidores().add(aliasSeguidor);
                usuarioSeguido.setC_seguidores(usuarioSeguido.getC_seguidores() + 1);  // Incrementar c_seguidores
            }

            // Guardar los cambios
            usuarioRepository.save(usuarioSeguidor);
            usuarioRepository.save(usuarioSeguido);
        }
    }

    public void dejarDeSeguirUsuario(String aliasSeguidor, String aliasSeguido){
        // Obtener el seguidor
        Optional<Usuario> optionalSeguidor = usuarioRepository.findByAlias(aliasSeguidor);
        Optional<Usuario> optionalSeguido = usuarioRepository.findByAlias(aliasSeguido);

        if (optionalSeguidor.isPresent() && optionalSeguido.isPresent()) {
            Usuario usuarioSeguidor = optionalSeguidor.get();
            Usuario usuarioSeguido = optionalSeguido.get();

            // Eliminar de la lista de seguidos del seguidor si está presente

            if (usuarioSeguidor.getSeguidos().contains(aliasSeguido)) {
                usuarioSeguidor.getSeguidos().remove(aliasSeguido);
                usuarioSeguidor.setC_seguidos(usuarioSeguidor.getC_seguidos() - 1);  // Incrementar c_seguidos
            }

            if (usuarioSeguido.getSeguidores().contains(aliasSeguidor)) {
                usuarioSeguido.getSeguidores().remove(aliasSeguidor);
                usuarioSeguido.setC_seguidores(usuarioSeguido.getC_seguidores() - 1);  // Incrementar c_seguidores
            }

            // Guardar cambios en la base de datos
            usuarioRepository.save(usuarioSeguidor);
            usuarioRepository.save(usuarioSeguido);
        }
    }

    public boolean verificaSiSigue(String aliasVisitante, String aliasPerfil) {
        // Buscar el usuario que está visitando el perfil
        Optional<Usuario> optionalVisitante = usuarioRepository.findByAlias(aliasVisitante);

        // Buscar el usuario dueño del perfil
        Optional<Usuario> optionalPerfil = usuarioRepository.findByAlias(aliasPerfil);

        if (optionalVisitante.isPresent() && optionalPerfil.isPresent()) {
            Usuario usuarioVisitante = optionalVisitante.get();

            // Verificar si el alias del perfil está en la lista de "seguidos" del visitante
            return usuarioVisitante.getSeguidos().contains(aliasPerfil);
        } else {
            // Si uno de los usuarios no existe, puedes lanzar una excepción o devolver false
            throw new IllegalArgumentException("Alias de usuario no encontrado");
        }
    }


    /*-----------------------------------------CRUD----------------------------------------*/

    public void eliminarPorIdUsuario(String id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> buscarPorAlias(String alias) {
        return this.usuarioRepository.findByAlias(alias);
    }

    /*Solo actualizar nombre y apellido, solo testeo*/
    public Usuario actualizarNombreApellidoPorAlias(String alias, String nombre, String apellido) {
        Optional<Usuario> userOptional = usuarioRepository.findByAlias(alias);
        Usuario usuario = new Usuario();
        if (userOptional.isPresent()) {
             usuario = userOptional.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
        } else {
            throw new NoSuchElementException("Usuario no encontrado");
        }
        return usuarioRepository.save(usuario);
    }


    public Usuario actualizarUsuario(String id, Usuario user) {

        Optional<Usuario> userOptional = buscarUsuarioPorId(id);
        if (userOptional.isPresent()) {

            Usuario usuarioExistente = userOptional.get();

            if (user.getNombre() != null) {
                if(user.getNombre().matches("^[a-zA-Z]+$")){
                    usuarioExistente.setNombre(user.getNombre());
                }else if (user.getNombre().matches("^[a-zA-Z]+$") == false) { /*
                           El método matches evalúa cadenas según lo que le indiquemos.
                           En este caso, solo quiero que tenga letras minúsculas y mayúsculas*/
                    throw new IllegalArgumentException("El nombre solo puede contener letras.");
                }
            }
            if (user.getApellido() != null) {
                if(user.getApellido().matches("^[a-zA-Z]+$")){
                    usuarioExistente.setApellido(user.getApellido());
                }else if (user.getNombre().matches("^[a-zA-Z]+$") == false) {
                    throw new IllegalArgumentException("El apellido solo puede contener letras.");
                }
            }
            if (user.getAlias() != null) {
                if(user.getAlias().matches("^[a-zA-Z0-9]+$")){

                }else if (user.getAlias().matches("^[a-zA-Z0-9]+$") == false){
                    throw new IllegalArgumentException("El alias solo puede contener letras y números.");
                }
                usuarioExistente.setAlias(user.getAlias());
            }

            if(user.getEmail() != null){
                usuarioExistente.setEmail(user.getEmail());
            }
            if (user.getTelefono() != null) {
                if(user.getTelefono().matches("^[0-9]+$")){
                    usuarioExistente.setTelefono(user.getTelefono());
                }else if (user.getTelefono().matches("^[0-9]+$")) {
                    throw new IllegalArgumentException("El teléfono solo puede contener números.");
               }
            }
            if (user.getUbicacion() != null){
                usuarioExistente.setUbicacion(user.getUbicacion());
            }
            return this.usuarioRepository.save(usuarioExistente);
        }else{
            throw new RuntimeException("Usuario no encontrado con el ID: " + id);
        }
    }

    public void actualizarContrasena(String id, String contrasena) {

        Query query2 = new Query(Criteria.where("_id").is(id));
        Update update2 = new Update().set("contrasena",contrasena);
        mongoTemplate.updateFirst(query2, update2, Usuario.class);
    }

    public Boolean actualizarRedes(String id, Redes redes) {
        if(this.usuarioRepository.existsById(id)){
            Query query = new Query(Criteria.where("_id").is(id));
            Update update = new Update().set("redes",redes);
            mongoTemplate.updateFirst(query, update, Usuario.class);
            return true;
        }else{return false;}
    }

    public void actualizarDescripcion(String id, String descripcion){
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("descripcion",descripcion);
        mongoTemplate.updateFirst(query, update, Usuario.class);
    }

    public Usuario actualizarInstrumentosUsuario(String idInstrumento, String idUsuario) {
        Optional<Usuario> usuarioOptional = this.buscarUsuarioPorId(idUsuario);
        Optional<Instrumento> instrumentoOptional = instrumentoService.buscarInstrumentoPorId(idInstrumento);

        if (!usuarioOptional.isPresent()) {
            throw new IllegalArgumentException("No existe usuario con la id: " + idUsuario);
        }

        if (!instrumentoOptional.isPresent()) {
            throw new IllegalArgumentException("No existe instrumento con la id: " + idInstrumento);
        }

        Usuario usuario = usuarioOptional.get();
        Instrumento instrumento = instrumentoOptional.get();

        List<Instrumento> instrumentos = usuario.getMisInstru();
        if (instrumentos == null) {
            instrumentos = new ArrayList<>();
        }

        // Verifica si el instrumento ya está en la lista
        if (instrumentos.stream().noneMatch(instr -> instr.getId().equals(instrumento.getId()))) {
            instrumentos.add(instrumento);
        } else {
            throw new IllegalArgumentException("El instrumento: " + instrumento.getNombre() + " ya está asignado al usuario.");
        }

        usuario.setMisInstru(instrumentos);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> buscarUsuariosPorInstrumentos(List<String> nombresInstrumentos) {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getMisInstru().stream()
                        .anyMatch(instr -> nombresInstrumentos.contains(instr.getNombre())))
                .collect(Collectors.toList());
    }

    public void actualizarMisIntereses(String id, List<String> intereses) {



    }

    /*
    public void crearPublicacion(String id, Publicacion publi) {
        Optional<Usuario> usu = usuarioRepository.findByAlias(id);
        if(usu != null){
            Usuario usu2 = usu.get();
            if(usu2.getMisPublicaciones().isEmpty()){
                usu2.getMisPublicaciones().add(publi);
                this.usuarioRepository.save(usu2);
            }else{
                List<Publicacion> misPublicaciones = new ArrayList<>();
                misPublicaciones.add(publi);
                usu2.setMisPublicaciones(misPublicaciones);
                this.usuarioRepository.save(usu2);
            }
        }
    }

     */

    public void crearBanda(String idUsuario, Banda b) {
        Optional<Usuario> usu = usuarioRepository.findById(idUsuario);
        if(usu != null){
            Usuario usu2 = usu.get();
            usu2.getMisBandas().add(b);
            usuarioRepository.save(usu2);
        }
    }

    public void añadirMiembro(String idMiembro, Banda banda2) {
        Optional<Usuario> usu = usuarioRepository.findById(idMiembro);
        if(usu != null){
            Usuario usu2 = usu.get();
            usu2.getMisBandas().add(banda2);
            usuarioRepository.save(usu2);
        }
    }

    public void eliminarMiembro(String idUsuario, Banda banda2) {
        Optional<Usuario> usu = usuarioRepository.findById(idUsuario);
        if(usu != null){
            Usuario usu2 = usu.get();
            usu2.getMisBandas().remove(banda2);
            usuarioRepository.save(usu2);
        }
    }

    public List<Publicacion> misLikesUsuario(String usuarioAlias) {
        List<MeGusta> meGustaList = this.iMeGustaRepository.findMeGustaByIdAlias(usuarioAlias);
        List<String> publicacionIds = new ArrayList<>();
        for (MeGusta meGusta : meGustaList){
            publicacionIds.add(meGusta.getPublicacionId());
        }
        return this.iPublicacionRepository.findAllById(publicacionIds);
    }

    public Boolean existByAlias(String alias) {
        return this.usuarioRepository.existsByAlias(alias);
    }

    //GUARDADO DE DATOS DEL ONBNOARDING
    public Usuario actualizarRolUsuarioPorAlias(String alias, String rol) {
        Optional<Usuario> userOptional = usuarioRepository.findByAlias(alias);
        if (userOptional.isPresent()) {
            Usuario usuario = userOptional.get();
            usuario.setRol(rol);
            return usuarioRepository.save(usuario);
        } else {
            throw new NoSuchElementException("Usuario no encontrado con alias: " + alias);
        }
    }
    

    public Usuario actualizarInstrumentosUsuarioPorAlias(String alias, List<String> instrumentoIds) {
        Optional<Usuario> userOptional = usuarioRepository.findByAlias(alias);
        if (userOptional.isPresent()) {
            Usuario usuario = userOptional.get();
    
            List<Instrumento> instrumentos = instrumentoIds.stream()
                    .map(id -> instrumentoService.buscarInstrumentoPorId(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
    
            usuario.setMisInstru(instrumentos);
            return usuarioRepository.save(usuario);
        } else {
            throw new NoSuchElementException("Usuario no encontrado con alias: " + alias);
        }
    }
    
    public Usuario actualizarGenerosUsuarioPorAlias(String alias, List<String> generoIds) {
        Optional<Usuario> userOptional = usuarioRepository.findByAlias(alias);
        if (userOptional.isPresent()) {
            Usuario usuario = userOptional.get();
    
            // Buscar los géneros musicales por sus IDs
            List<GeneroMusical> generos = generoIds.stream()
                    .map(id -> generoMusicalService.buscarGeneroPorId(id))
                    .filter(Optional::isPresent) // Filtro para evitar nulos
                    .map(Optional::get)
                    .collect(Collectors.toList());
    
            if (generos.isEmpty()) {
                throw new NoSuchElementException("No se encontraron géneros musicales con los IDs proporcionados");
            }
    
            usuario.setMisGeneros(generos); // Actualizar la lista de géneros musicales del usuario
            return usuarioRepository.save(usuario);
        } else {
            throw new NoSuchElementException("Usuario no encontrado con alias: " + alias);
        }
    }
    

    
    
    // UsuarioService.java
    public Usuario actualizarDescripcionUsuarioPorAlias(String alias, String descripcion) {
        Optional<Usuario> userOptional = usuarioRepository.findByAlias(alias);
        if (userOptional.isPresent()) {
            Usuario usuario = userOptional.get();
            usuario.setDescripcion(descripcion); // Actualizamos la descripción
            return usuarioRepository.save(usuario); // Guardamos los cambios en la base de datos
        } else {
            throw new NoSuchElementException("Usuario no encontrado con alias: " + alias);
        }
    }
    
    
    //Carga del Onboarding    
    public Usuario completarOnboarding(
    String alias, 
    String rol, 
    List<String> instrumentoIds, 
    List<String> generoIds, 
    String descripcion, 
    MultipartFile file) 
    {
    Optional<Usuario> userOptional = usuarioRepository.findByAlias(alias);
    if (!userOptional.isPresent()) {
        throw new NoSuchElementException("Usuario no encontrado con alias: " + alias);
    }

    Usuario usuario = userOptional.get();

    // Actualizar rol
    if (rol != null && !rol.isEmpty()) {
        usuario.setRol(rol);
    }

    // Actualizar instrumentos
    if (instrumentoIds != null && !instrumentoIds.isEmpty()) {
        List<Instrumento> instrumentos = instrumentoIds.stream()
                .map(id -> instrumentoService.buscarInstrumentoPorId(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        usuario.setMisInstru(instrumentos);
    }

    // Actualizar géneros musicales
    if (generoIds != null && !generoIds.isEmpty()) {
        List<GeneroMusical> generos = generoIds.stream()
                .map(id -> generoMusicalService.buscarGeneroPorId(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        usuario.setMisGeneros(generos);
    }

    // Actualizar descripción
    if (descripcion != null && !descripcion.isEmpty()) {
        usuario.setDescripcion(descripcion);
    }

    // Subir foto de perfil
    if (file != null && !file.isEmpty()) {
        try {
            String fileUrl = s3Service.uploadFile(file);
            usuario.setFotoPerfilUrl(fileUrl);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al subir la foto de perfil: " + e.getMessage());
        }
    }

    // Guardar cambios en la base de datos
    return usuarioRepository.save(usuario);
}






}
