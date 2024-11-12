package com.project.meetsounds.controllers;

import com.project.meetsounds.domain.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.project.meetsounds.services.SeguidosService;

import java.util.List;
import java.util.Set;

@Controller
public class SeguidosController {
    @Autowired
    private SeguidosService seguidosService;

    @QueryMapping(name = "misSeguidos")
    public List<Usuario> misSeguidos(String idUsuario){
        return this.seguidosService.misSeguidos(idUsuario);
    }

    @MutationMapping(name = "Seguir")
    public void seguirUsuario(@Argument String idUsuario, @Argument String idSeguir){ //idSeguir es del usuario al cual vamos a seguir
        this.seguidosService.seguirUsuario(idUsuario, idSeguir);
    }

    @MutationMapping(name = "DejarDeSeguir")
    public void dejarDeSeguir(@Argument String idUsuario, @Argument String idSeguido){ //idSeguido es la id del que vamos a dejar de seguir
        this.seguidosService.dejarDeSegir(idUsuario, idSeguido);
    }
}
