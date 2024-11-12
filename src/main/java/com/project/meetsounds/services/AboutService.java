package com.project.meetsounds.services;

import com.project.meetsounds.domain.models.About;
import com.project.meetsounds.repositories.IAboutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AboutService {

    @Autowired
    private IAboutRepository aboutRepository;

    public About guardarAbout(String usuarioId, String rol, List<String> instrumentos, List<String> generoMus, String descripcion) {
        About about = new About();
        about.setUsuarioId(usuarioId);
        about.setRol(rol);
        about.setInstrumentos(instrumentos);
        about.setGeneroMus(generoMus);
        about.setDescripcion(descripcion);

        return aboutRepository.save(about);
    }
}
