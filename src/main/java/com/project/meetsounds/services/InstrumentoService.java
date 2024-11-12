package com.project.meetsounds.services;

import com.project.meetsounds.domain.models.Instrumento;
import com.project.meetsounds.domain.models.TipoInstrumento;
import com.project.meetsounds.repositories.IInstrumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstrumentoService {

    @Autowired
    IInstrumentoRepository iInstrumentoRepository;

    @Autowired
    TipoInstrumentoService tipoInstrumentoService;

    public Instrumento guardarInstrumento(Instrumento instrumento, String tipo){
        instrumento.setTipoInstrumento(tipoInstrumentoService.buscarTipoInstrumentoPorNombre(tipo));
        return iInstrumentoRepository.save(instrumento);
    }

    public List<Instrumento> traerTodosLosInstrumentos(){
        return iInstrumentoRepository.findAll();
    }

    public List<Instrumento> traerInstrumentosPorTipo(String tipo){
        return iInstrumentoRepository.findAll().stream()
                .filter(instrumento -> instrumento.getTipoInstrumento().getNombre().equals(tipo))
                .collect(Collectors.toList());
    }

    public void eliminarInstrumentoPorId(String id){
        iInstrumentoRepository.deleteById(id);
    }

    public Optional<Instrumento> buscarInstrumentoPorId(String id){
        return iInstrumentoRepository.findById(id);
    }

    public Optional<Instrumento> buscarInstrumentoPorNombre(String nombre){
        return iInstrumentoRepository.findByNombre(nombre);
    }
}
