package com.project.meetsounds.controllers;


import com.project.meetsounds.domain.models.Instrumento;
import com.project.meetsounds.services.InstrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class InstrumentoController {

    @Autowired
    InstrumentoService instrumentoService;

    @MutationMapping(name = "guardarInstrumento")
    public Instrumento guardarInstrumento(@Argument Instrumento instrumento, @Argument String tipo){
        return instrumentoService.guardarInstrumento(instrumento,tipo);
    }
    @QueryMapping(name = "buscarTodosLosInstrumentos")
    public List<Instrumento> traerTodosLosInstrumentos(){
        return instrumentoService.traerTodosLosInstrumentos();
    }
    @QueryMapping(name = "buscarInstrumentosPorTipo")
    public List<Instrumento> traerInstrumentosPorTipo(@Argument String tipo){
        return instrumentoService.traerInstrumentosPorTipo(tipo);
    }
    @MutationMapping(name = "eliminarInstrumentoPorId")
    public void eliminarInstrumentoPorId(@Argument String id){
        instrumentoService.eliminarInstrumentoPorId(id);
    }
    @QueryMapping(name = "buscarInstrumentoPorNombre")
    public Optional<Instrumento> buscarInstrumentoPorNombre(@Argument String nombre){
        return instrumentoService.buscarInstrumentoPorNombre(nombre);
    }
}
