package com.project.meetsounds.services;

import com.project.meetsounds.domain.models.TipoInstrumento;
import com.project.meetsounds.repositories.ITipoInstrumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoInstrumentoService {

    @Autowired
    ITipoInstrumentoRepository iTipoInstrumentoRepository;

    public TipoInstrumento guardarTipoInstrumento(TipoInstrumento tipoInstrumento){
        return iTipoInstrumentoRepository.save(tipoInstrumento);
    }
    public List<TipoInstrumento> traerTodosLosTiposInstrumento(){
        return iTipoInstrumentoRepository.findAll();
    }
    public TipoInstrumento buscarTipoInstrumentoPorNombre(String nombre){
        return iTipoInstrumentoRepository.findByNombre(nombre);
    }

    public void eliminarTipoInstrumentoPorId(String id){
        iTipoInstrumentoRepository.deleteById(id);
    }

}
