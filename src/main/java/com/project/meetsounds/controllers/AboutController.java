package com.project.meetsounds.controllers;

import com.project.meetsounds.domain.models.About;
import com.project.meetsounds.services.AboutService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AboutController {

    @Autowired
    private AboutService aboutService;

    @MutationMapping
    public About guardarAbout(
        @Argument String usuarioId,
        @Argument String rol,
        @Argument List<String> instrumentos,
        @Argument List<String> generoMus,
        @Argument String descripcion
    ) {
        return aboutService.guardarAbout(usuarioId, rol, instrumentos, generoMus, descripcion);
    }
}
