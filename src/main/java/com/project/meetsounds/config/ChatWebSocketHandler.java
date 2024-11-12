package com.project.meetsounds.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.meetsounds.domain.models.Chat;
import com.project.meetsounds.domain.models.Mensaje;
import com.project.meetsounds.services.ChatService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<WebSocketSession> activeSessions = ConcurrentHashMap.newKeySet(); // Sesiones activas

    public ChatWebSocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        activeSessions.add(session); // Agregamos la sesión activa
        System.out.println("Nueva conexión: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        activeSessions.remove(session); // Quitamos la sesión cuando se desconecta
        System.out.println("Conexión cerrada: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Convierte el mensaje recibido de JSON a objeto Mensaje
        Mensaje mensaje = objectMapper.readValue(message.getPayload(), Mensaje.class);
        mensaje.setFechaEnvio(LocalDateTime.now());

        // Guardar el mensaje en el chat correspondiente
        Chat chat = chatService.buscarChatPorId(mensaje.getIdChat());
        if (chat == null) {
            session.sendMessage(new TextMessage("Error: El chat no existe."));
            return;
        }
        chatService.guardarMensaje(chat.getId(), mensaje);

        // Enviar el mensaje a todas las sesiones activas
        broadcastMessage(mensaje);
    }

    private void broadcastMessage(Mensaje mensaje) throws IOException {
        String respuesta = objectMapper.writeValueAsString(mensaje);
        for (WebSocketSession session : activeSessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(respuesta));
            }
        }
    }
}
