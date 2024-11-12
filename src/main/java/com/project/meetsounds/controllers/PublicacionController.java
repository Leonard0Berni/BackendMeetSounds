package com.project.meetsounds.controllers;


import com.project.meetsounds.domain.models.Publicacion;

import com.project.meetsounds.domain.models.PublicacionOut;
import com.project.meetsounds.domain.models.Usuario;
import com.project.meetsounds.services.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;


    @GetMapping("/listarPublicaciones")
    public Page<Publicacion> listarPublicaciones(@RequestParam int page,@RequestParam int size){
        return publicacionService.listarPublicaciones(page, size);
    }
    @GetMapping("/listarMultimediaUsuario")
    public Page<Publicacion> listarMultimediaUsuario(@RequestParam String alias,@RequestParam int page,@RequestParam int size){
        return publicacionService.listarMultimediaUsuario(alias, page, size);
    }
    @GetMapping("/listarPosteosUsuario")
    public Page<Publicacion> listarPosteosUsuario(@RequestParam String alias,@RequestParam int page,@RequestParam int size){
        return publicacionService.listarPosteosUsuario(alias, page, size);
    }
    @GetMapping("/listarPublicacionesUsuario")
    public Page<Publicacion> listarPublicacionesUsuario(@RequestParam String alias,@RequestParam int page,@RequestParam int size){
        return publicacionService.listarPublicacionesUsuario(alias, page, size);
    }

    @GetMapping("/buscarPublicacionPorId")
    public Publicacion buscarPublicacionPorId(@RequestParam String id){
        return publicacionService.buscarPublicacionPorId(id);
    }
    @GetMapping("/comprobarEsDuenoPublicacion")
    public boolean comprobarEsDuenoPublicacion(@RequestParam String idPublicacion,@RequestParam String usuarioAlias){
        return publicacionService.comprobarEsDuenoPublicacion(idPublicacion,usuarioAlias);
    }
    @GetMapping("/contarPublicacionesUsuario")
    public int contarPublicacionesUsuario(@RequestParam String idAlias){
        return publicacionService.contarPublicacionesUsuario(idAlias);
    }

    @PostMapping("/darMeGusta")
        public Boolean darMeGusta(@RequestParam String idPublicacion, @RequestParam String usuarioAlias){
        return this.publicacionService.darMeGusta(idPublicacion, usuarioAlias);
    }

    @PostMapping("/quitarMeGusta")
    public Boolean quitarMeGusta(@RequestParam String idPublicacion, @RequestParam String usuarioAlias){
        return this.publicacionService.quitarMeGusta(idPublicacion, usuarioAlias);
    }

    @PostMapping("/usuarioHaDadoMeGusta")
    public Boolean usuarioHaDadoMeGusta(@RequestParam String idPublicacion, @RequestParam String usuarioAlias){
        return this.publicacionService.usuarioHaDadoMeGusta(idPublicacion, usuarioAlias);
    }
    /*
    @MutationMapping(name = "crearPublicacion")
    public Publicacion crearPublicacion(@Argument String idAlias, @Argument String descripcion, @Argument MultipartFile file){
        return publicacionService.crearPublicacion(idAlias, descripcion, file);
    }

     */


    @PostMapping("/crearPublicacion")
    public void crearPublicacion(@RequestParam String idAlias, @RequestParam(required=false) String descripcion, @RequestParam(required=false) MultipartFile file) {
        publicacionService.crearPublicacion(idAlias, descripcion, file);
    }




    @DeleteMapping("/eliminarPublicacionPorId")
    public ResponseEntity<Void> eliminarPublicacionPorId(@RequestParam String idAlias, @RequestParam String idPublicacion) {
        try {
            publicacionService.eliminarPublicacion(idAlias, idPublicacion);
            return ResponseEntity.ok().build(); // Responde con 200 OK si la eliminación fue exitosa
        } catch (Exception e) {
            // Si ocurre un error, respondemos con 400 Bad Request o 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





}
