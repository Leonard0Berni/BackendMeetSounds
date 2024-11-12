package com.project.meetsounds.controllers;

import com.project.meetsounds.domain.models.Usuario;
import com.project.meetsounds.services.BandaService;
import com.project.meetsounds.services.SeguidoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SeguidoresController {
    @Autowired
    private SeguidoresService seguidoresService;

    @Autowired
    private BandaService bandaService;

    @QueryMapping(name = "misSeguidores")
    public List<Usuario> misSeguidores(@Argument String idAlias){
        return this.seguidoresService.misSeguidores(idAlias);
    }

    @QueryMapping(name = "misSeguidoresBanda") //Trae los usuario que siguen a tal banda
    public List<Usuario> misSeguidoresBanda(@Argument String idBanda){
        return this.bandaService.misSeguidores(idBanda);
    }

    @MutationMapping(name = "eliminarSeguidor")
    public void eliminarSeguidor(@Argument String idAliasUsuario, @Argument String idAliasSeguidor){
        this.seguidoresService.eliminarSeguidor(idAliasUsuario, idAliasSeguidor);
    }
}
