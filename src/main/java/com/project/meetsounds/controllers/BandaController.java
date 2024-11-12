package com.project.meetsounds.controllers;


import com.project.meetsounds.controlErrores.BandaYaExisteException;
import com.project.meetsounds.domain.models.Banda;
import com.project.meetsounds.domain.models.Usuario;
import com.project.meetsounds.services.BandaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;

@RestController
public class BandaController {

    @Autowired
    private BandaService bandaService;

    @QueryMapping(name = "listarBandas")
    public List<Banda> listarBandas(){
        return bandaService.listarBandas();
    }

    @QueryMapping(name = "listarBandaPorUsuario")
    public List<Banda> listarBandaPorUsuario(@Argument String idUsuario){
        return this.bandaService.listarBandasPorUsuario(idUsuario);
    }

    @PostMapping("/actualizarFotoBanda")
    public Boolean actualizarFotoBanda(@RequestParam String idBanda, @RequestParam MultipartFile file){
        return this.bandaService.actualizarFotoPortada(idBanda, file);

    }



    @QueryMapping(name = "buscarBandaPorId")
    public Optional<Banda> buscarBandaPorId(@Argument String idBanda){
        return bandaService.buscarBandaPorId(idBanda);
    }

    @QueryMapping(name = "listarBandasPorNombre")
    public List<Banda> listarBandasPorNombre(@Argument String nombreBanda){
        return bandaService.listarPorNombre(nombreBanda);
    }

    @QueryMapping(name = "buscarBandaPorNombre")
    public Optional<Banda> buscarBandaPorNombre(@Argument String nombreBanda){
        return bandaService.buscarBandaPorNombre(nombreBanda);
    }

    @MutationMapping(name = "crearBanda")
    public Banda crearBanda(@Argument String idUsuario, @Argument Banda banda){
        if(!this.buscarBandaPorNombre(banda.getNombreBanda()).isEmpty()){
            throw new BandaYaExisteException("Ya existe una banda con ese nombre");
        }
        return bandaService.crearBanda(idUsuario, banda);
    }

    @MutationMapping(name = "eliminarBanda")
    public void eliminarBanda(@Argument String id){
        bandaService.eliminarBanda(id);
    }

    @MutationMapping(name = "actualizarDescripcionBanda")
    public void actualizarDescripcion(@Argument String idBanda, @Argument String descripcion){
        bandaService.actualizarDescripcion(idBanda, descripcion);
    }

    @MutationMapping(name = "anadirMiembros")
    public void anadirMiembros(@Argument String nombreBanda, @Argument List<String> idUsuarios){
        bandaService.anadirMiembros(nombreBanda, idUsuarios);
    }

    @MutationMapping(name = "eliminarMiembro")
    public void eliminarMiembro(@Argument String idBanda, @Argument String idUsuario){
        bandaService.eliminarMiembro(idBanda, idUsuario);
    }

    @MutationMapping(name = "abandonarBanda")
    public void abandonarBanda(@Argument String idBanda, @Argument String idAlias){
        this.bandaService.abandonarBanda(idBanda, idAlias);
    }

    @MutationMapping(name = "seguirBanda")
    public Boolean seguirBanda(@Argument String idBanda, @Argument String aliasUsuario){
        return this.bandaService.seguirBanda(idBanda, aliasUsuario);
    }


    @MutationMapping
    public Boolean dejarDeSeguirBanda(@Argument String idBanda, @Argument String aliasUsuario){
        return this.bandaService.dejarDeSeguirBanda(idBanda, aliasUsuario);
    }

    @PostMapping("/subirContenido")
    public Boolean subirContenido(@RequestParam String idBanda, @RequestParam MultipartFile file){
        return this.bandaService.subirContenido(idBanda, file);
    }


}
