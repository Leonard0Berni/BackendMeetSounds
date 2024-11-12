package com.project.meetsounds.controllers;

import com.project.meetsounds.domain.models.TipoInstrumento;
import com.project.meetsounds.services.TipoInstrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TipoInstrumentoController{
    @Autowired
    TipoInstrumentoService tipoInstrumentoService;

    @MutationMapping(name = "guardarTipoInstrumento")
    public TipoInstrumento guardarTipoInstrumento(@Argument TipoInstrumento tipoInstrumento){
        return tipoInstrumentoService.guardarTipoInstrumento(tipoInstrumento);
    }

    @QueryMapping(name = "buscarTodosLosTiposInstrumento")
    public List<TipoInstrumento> traerTodosLosTiposInstrumento(){
        return tipoInstrumentoService.traerTodosLosTiposInstrumento();
    }

    @MutationMapping(name = "eliminarTipoInstrumentoPorId")
    public void eliminarTipoInstrumentoPorId(@Argument String id){
        tipoInstrumentoService.eliminarTipoInstrumentoPorId(id);
    }
}
