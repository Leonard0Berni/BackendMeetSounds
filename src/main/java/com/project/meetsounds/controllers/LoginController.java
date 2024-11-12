package com.project.meetsounds.controllers;


import com.project.meetsounds.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticacion")
public class LoginController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session){
        boolean loginExitoso = usuarioService.loginUsuario(username, password);
        if (loginExitoso) {
            System.out.println(username+" "+password);
            // Guardar usuario en la sesión
            session.setAttribute("usuario", username);
            return "Login exitoso";
        } else {
            return "Credenciales incorrectas";
        }
    }

    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();  // Invalidar la sesión actual
    }

    @GetMapping("/perfil")
    public Boolean comprobarLogin(HttpSession session) {
        String username = (String) session.getAttribute("usuario");

        if (username != null) {
            return true;
        } else {
            return false;
        }
    }

}
