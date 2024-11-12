package com.project.meetsounds.controllers;

import com.project.meetsounds.domain.models.ComentarioOut;
import com.project.meetsounds.services.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ComentarioController {
    @Autowired
    private ComentarioService comentarioService;

    @PostMapping("/Comentar")
    public void comentar(@RequestParam String publicacionId, @RequestParam String idAliasUsuario, @RequestParam String text){
        comentarioService.comentar(publicacionId, idAliasUsuario, text);
    }

    @QueryMapping(name = "listarComentariosPorId")
    public List<ComentarioOut> listarComentariosPorId(@Argument String publicacionId){
        return this.comentarioService.listarComentariosPorId(publicacionId);
    }
}
