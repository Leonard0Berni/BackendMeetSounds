package com.project.meetsounds.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.project.meetsounds.domain.models.Usuario;
import com.project.meetsounds.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.meetsounds.domain.models.Seguido;
import com.project.meetsounds.repositories.ISeguirRepository;


@Service
public class SeguidosService {

    @Autowired
    private ISeguirRepository iSeguirRepository;

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    public void seguirUsuario(String idUsuario, String idSeguir) {
        try {
            Seguido seguido = iSeguirRepository.findByidUsuario(idUsuario);
            List<String> misSeguidos = new ArrayList<>();
            misSeguidos = seguido.getMisSeguidos();
            misSeguidos.add(idSeguir);
            seguido.setMisSeguidos(misSeguidos);
            iSeguirRepository.save(seguido);

            //Incrementamos el contador de seguidos en el usuario
            Optional<Usuario> usu = this.iUsuarioRepository.findById(idUsuario);

            if (usu.isPresent()){
                Usuario usuario = usu.get();
                int countSeguidos = usuario.getC_seguidos();
                countSeguidos += 1;
                usuario.setC_seguidos(countSeguidos);
                this.iUsuarioRepository.save(usuario);

            }
        }catch (NullPointerException n){
            Optional<Usuario> usuarioOptional = this.iUsuarioRepository.findById(idUsuario);
            if (usuarioOptional.isPresent()){
                Usuario usuario = usuarioOptional.get();
                Seguido seguido = new Seguido();
                seguido.setUser(usuario);
                seguido.getMisSeguidos().add(idSeguir);
                this.iSeguirRepository.save(seguido);
            }

        }
    }

    public void dejarDeSegir(String idUsuario, String idSeguido) {
        Seguido seguido = iSeguirRepository.findByidUsuario(idUsuario);
        List<String> misSeguidos = new ArrayList<>();
        misSeguidos = seguido.getMisSeguidos();

        for (String string : misSeguidos) {
            if (string.equals(idSeguido)) {
                misSeguidos.remove(string);            
            }
        }
        
        seguido.setMisSeguidos(misSeguidos);
        iSeguirRepository.save(seguido);

        //Disminuimos el contador de seguidos en el usuario
        Optional<Usuario> usu = this.iUsuarioRepository.findById(idUsuario);

        if (usu.isPresent()){
            Usuario usuario = usu.get();
            int countSeguidos = usuario.getC_seguidos();
            countSeguidos =- 1;
            usuario.setC_seguidos(countSeguidos);
            this.iUsuarioRepository.save(usuario);

        }
    }

    public List<Usuario> misSeguidos(String idUsuario) {
        Seguido seguido = this.iSeguirRepository.findByidUsuario(idUsuario);
        List<Usuario> usuariosSeguidos = this.iUsuarioRepository.findAllById(seguido.getMisSeguidos());
        return usuariosSeguidos;
    }
}
