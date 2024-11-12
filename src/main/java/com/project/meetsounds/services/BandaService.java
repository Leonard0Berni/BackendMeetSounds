package com.project.meetsounds.services;

import com.project.meetsounds.domain.models.Banda;
import com.project.meetsounds.domain.models.Usuario;
import com.project.meetsounds.repositories.IBandaRepository;
import com.project.meetsounds.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import java.util.List;
import java.util.Optional;


@Service
public class BandaService {

    @Autowired
    private IBandaRepository iBandaRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public List<Banda> listarBandas() {
        return iBandaRepository.findAll();
    }

    public List<Banda> listarPorNombre(String nombreBanda) {
        return iBandaRepository.listarPorNombre(nombreBanda);
    }

    public Optional<Banda> buscarBandaPorId(String idBanda) {
        return iBandaRepository.findById(idBanda);
    }

    public Optional<Banda> buscarBandaPorNombre(String nombre){
        return iBandaRepository.findByNombreBanda(nombre);
    }
    public Banda crearBanda(String idUsuario, Banda bandaInput) {

        // Aquí `banda` ya tiene los IDs de los miembros en la lista `miembros`
        Banda banda = new Banda();
        banda.setNombreBanda(bandaInput.getNombreBanda());
        banda.setDescripcion(bandaInput.getDescripcion());
        banda.setIdCreador(idUsuario);
        if (banda.getMiembros() == null || banda.getMiembros().isEmpty()) {
            for(String idMiembro : bandaInput.getMiembros()){
                banda.getMiembros().add(idMiembro);
            }
        }
        return iBandaRepository.save(banda);
    }

    public void eliminarBanda(String id){
        iBandaRepository.deleteById(id);
    }

    public void actualizarDescripcion(String idBanda, String descripcion) {
        Query query = new Query(Criteria.where("_id").is(idBanda));
        Update update = new Update().set("descripcion",descripcion);
        mongoTemplate.updateFirst(query, update, Banda.class);
    }

    public void anadirMiembros(String nombreBanda, List<String> idUsuarios) {
        Optional<Banda> banda = this.buscarBandaPorNombre(nombreBanda);
        Banda banda2 = banda.orElseThrow(()-> new IllegalArgumentException("Banda no encontrada"));
        for(String idMiembro : idUsuarios){
            banda2.getMiembros().add(idMiembro);
            usuarioService.añadirMiembro(idMiembro, banda2);
            iBandaRepository.save(banda2);
        }
    }

    public void eliminarMiembro(String idBanda, String idUsuario) {
        Optional<Banda> banda = iBandaRepository.findById(idBanda);
        Banda banda2 = banda.orElseThrow(()-> new IllegalArgumentException("Banda no encontrada"));
        banda2.getMiembros().remove(idUsuario);
        usuarioService.eliminarMiembro(idUsuario, banda2);
        iBandaRepository.save(banda2);
    }

    public void abandonarBanda(String idBanda, String idAlias) {
        Optional<Banda> bandaOptional = this.iBandaRepository.findById(idBanda);
        Banda banda = bandaOptional.orElseThrow(()-> new IllegalArgumentException("Banda no encontrada"));

        //Cada usuario tiene la lista de Bandas a la que pertenece, por eso hay que traer las
        // listas de todos los usuarios que pertenecen a la banda para actualizar todas sus listas.
        for (String idAliasUsuario : banda.getMiembros()){
            Optional<Usuario> usuarioOptional = this.usuarioService.buscarPorAlias(idAliasUsuario);
            Usuario usuario = usuarioOptional.orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));

            // Trae la lista de bandas de cada usuario y elimina el usuario que salio de la banda.
            usuario.getMisBandas().removeIf(b -> b.getMiembros().stream().anyMatch(u -> u.equals(idAlias)));
            this.iUsuarioRepository.save(usuario);
        }

        banda.getMiembros().remove(idAlias);
        this.iBandaRepository.save(banda);
    }

    public List<Banda> listarBandasPorUsuario(String idUsuario) {
        System.out.println(idUsuario);
        return this.iBandaRepository.listarBandasPorUsuario(idUsuario);
    }

    public Boolean actualizarFotoPortada(String idBanda, MultipartFile file) {
        try {
            String urlFotoPortada = this.s3Service.uploadFile(file);
            Optional<Banda> bandaOptional = this.iBandaRepository.findById(idBanda);
            Banda banda = bandaOptional.orElseThrow(()-> new NullPointerException("No se ha encontrado la banda con id: " + idBanda));
            banda.setUrlFotoBanda(urlFotoPortada);
            this.iBandaRepository.save(banda);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public Boolean subirContenido(String idBanda, MultipartFile file) {
        Optional<Banda> bandaOptional = this.iBandaRepository.findById(idBanda);
        try {
            Banda banda = bandaOptional.orElseThrow(()->new IllegalArgumentException("No se ha encontrado la banda con id: " + idBanda));
            String urlContenido = this.s3Service.uploadFile(file);
            banda.getGaleria().add(urlContenido);
            this.iBandaRepository.save(banda);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public Boolean seguirBanda(String idBanda, String aliasUsuario) {
        Optional<Banda> bandaOptional = this.iBandaRepository.findById(idBanda);
        Banda banda = bandaOptional.orElseThrow(()-> new IllegalArgumentException("No se ha encontrado la banda con id: " + idBanda));
        banda.getSeguidores().add(aliasUsuario);
        this.iBandaRepository.save(banda);
        return true;
    }

    public List<Usuario> misSeguidores(String idBanda) {
        Optional<Banda> bandaOptional = this.iBandaRepository.findById(idBanda);
        Banda banda = bandaOptional.orElseThrow(()-> new IllegalArgumentException("No se ha encontrado la banda con id: " + idBanda));
        return this.iUsuarioRepository.findAllByAlias(banda.getSeguidores());
    }


    public Boolean dejarDeSeguirBanda(String idBanda, String aliasUsuario) {
        Optional<Banda> bandaOptional = this.iBandaRepository.findById(idBanda);
        Banda banda = bandaOptional.orElseThrow(()-> new IllegalArgumentException("No se ha encontrado la banda con id: " + idBanda));
        banda.getSeguidores().remove(aliasUsuario);
        this.iBandaRepository.save(banda);
        return true;
    }
}
