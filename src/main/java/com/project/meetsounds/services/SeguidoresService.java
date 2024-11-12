package com.project.meetsounds.services;

import com.project.meetsounds.domain.models.Usuario;
import com.project.meetsounds.repositories.ISeguidoresRepository;
import com.project.meetsounds.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeguidoresService {
    @Autowired
    private ISeguidoresRepository iSeguidoresRepository;

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    public List<Usuario> misSeguidores(String idAlias) {
        Optional<Usuario> usuarioOptional = this.iUsuarioRepository.findByAlias(idAlias);
        Usuario usuario = new Usuario();

        if (usuarioOptional.isPresent()){
            usuario = usuarioOptional.get();
        }

        return this.iUsuarioRepository.findAllByAlias(usuario.getSeguidores());
    }

    public void eliminarSeguidor(String idAliasUsuario, String idAliasSeguidor) {

        // Nesecito eliminar el seguidor de la lista de seguidores del Usuario
        Optional<Usuario> usuarioOptional = this.iUsuarioRepository.findByAlias(idAliasUsuario);
        Usuario usuario = new Usuario();
        if (usuarioOptional.isPresent()){
            usuario = usuarioOptional.get();
        }

        // Nesecito eliminar el usuario de la lista de seguidos del Seguidor
        Optional<Usuario> seguidorOptional = this.iUsuarioRepository.findByAlias(idAliasSeguidor);
        Usuario seguidor = new Usuario();
        if (usuarioOptional.isPresent()){
            seguidor = usuarioOptional.get();
        }

        usuario.getSeguidores().removeIf(s-> s.equals(idAliasSeguidor));
        seguidor.getSeguidos().removeIf(s -> s.equals(idAliasUsuario));
        this.iUsuarioRepository.save(usuario);
        this.iUsuarioRepository.save(seguidor);

    }
}
