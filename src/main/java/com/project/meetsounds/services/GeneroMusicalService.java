package com.project.meetsounds.services;

import com.project.meetsounds.domain.models.GeneroMusical;
import com.project.meetsounds.repositories.IGeneroMusicalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeneroMusicalService {

    @Autowired
    private IGeneroMusicalRepository generoMusicalRepository;

    public List<GeneroMusical> listarTodosLosGeneros() {
        return generoMusicalRepository.findAll();
    }

    public Optional<GeneroMusical> buscarGeneroPorId(String id) {
        return generoMusicalRepository.findById(id);
    }

    public GeneroMusical guardarGenero(GeneroMusical generoMusical) {
        return generoMusicalRepository.save(generoMusical);
    }

    public void eliminarGenero(String id) {
        generoMusicalRepository.deleteById(id);
    }
}
