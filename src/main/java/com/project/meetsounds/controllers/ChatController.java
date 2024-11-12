package com.project.meetsounds.controllers;

import com.project.meetsounds.domain.models.Chat;
import com.project.meetsounds.domain.models.Mensaje;
import com.project.meetsounds.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/enviarMensaje/{idChat}")
    @SendTo("/topic/mensajes/{idChat}")
    public Mensaje enviarMensaje(Mensaje mensaje) {
        mensaje.setFechaEnvio(LocalDateTime.now());

        // Guardar el mensaje en el chat correspondiente
        Chat chat = chatService.buscarChatPorId(mensaje.getIdChat());
        if (chat == null) {
            throw new RuntimeException("El chat no existe.");
        }

        chatService.guardarMensaje(chat.getId(), mensaje);
        return mensaje;
    }

    @PostMapping("/iniciarChat")
    public ResponseEntity<Chat> iniciarChat(@RequestParam String usuario1, @RequestParam String usuario2) {
        System.out.println("Usuario1: " + usuario1 + ", Usuario2: " + usuario2);
        Chat nuevoChat = chatService.iniciarChat(usuario1, usuario2);
        return ResponseEntity.ok(nuevoChat);
    }

    @GetMapping("/traerTodosLosChats")
    public ResponseEntity<List<Chat>> traerTodosLosChats() {
        List<Chat> chats = chatService.traerTodosLosChats();
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/traerTodosLosChatsxIdUsuario")
    public ResponseEntity<List<Chat>> traerTodosLosChatsxIdUsuario(@RequestParam String idUsuario) {
        List<Chat> chats = chatService.traerChatsPorUsuarioId(idUsuario);

        if (chats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        return ResponseEntity.ok(chats);
    }
    @GetMapping("/buscarChatPorId")
    public Chat buscarChatPorId(@RequestParam String idChat){
        return chatService.buscarChatPorId(idChat);
    }

}
