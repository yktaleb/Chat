package com.chat.websocket;

import com.chat.model.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;

@ApplicationScoped
public class UserSessionHandler {

    private HashSet<Session> liveSessions = new HashSet<Session>();

    public void addSession(Session session) {
        liveSessions.add(session);
    }

    public void removeSession(Session session) {
        liveSessions.remove(session);
    }

    public void sendMessageToAll(Message message) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add(Message.JSON_NAME_NICK_NAME, message.getNickName())
                .add(Message.JSON_NAME_CONTENT, message.getContent())
                .build();
        for (Session session : liveSessions) {
            sendToSession(session, jsonMessage);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException e) {
            liveSessions.remove(session);
        }
    }

}
