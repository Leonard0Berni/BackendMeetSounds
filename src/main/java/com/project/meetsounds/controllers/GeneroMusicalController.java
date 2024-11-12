package com.project.meetsounds.controllers;

import com.project.meetsounds.domain.models.GeneroMusical;
import com.project.meetsounds.services.GeneroMusicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class GeneroMusicalController {

    @Autowired
    private GeneroMusicalService generoMusicalService;

    @QueryMapping(name = "listarTodosLosGeneros")
    public List<GeneroMusical> listarTodosLosGeneros() {
        return generoMusicalService.listarTodosLosGeneros();
    }

    @QueryMapping(name = "buscarGeneroPorId")
    public Optional<GeneroMusical> buscarGeneroPorId(@Argument String id) {
        return generoMusicalService.buscarGeneroPorId(id);
    }

    @MutationMapping(name = "guardarGenero")
    public GeneroMusical guardarGenero(@Argument String nombre) {
        GeneroMusical generoMusical = new GeneroMusical();
        generoMusical.setNombre(nombre);
        return generoMusicalService.guardarGenero(generoMusical);
    }

    @MutationMapping(name = "eliminarGenero")
    public void eliminarGenero(@Argument String id) {
        generoMusicalService.eliminarGenero(id);
    }
}
