package com.chat.websocket;

import com.chat.model.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.StringReader;

@ApplicationScoped
@ServerEndpoint("/chat/{room}")
public class ChatServer {

    @Inject
    private UserSessionHandler sessionHandler;

    @OnOpen
    public void open(Session session, @PathParam("room") String room) {
        session.getUserProperties().put("room", room);
        sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {

    }

    @OnMessage
    public void handleMessage(Session session, String message) {
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonMessage = reader.readObject();
        Message msg = new Message(
                jsonMessage.getString(Message.JSON_NAME_NICK_NAME),
                jsonMessage.getString(Message.JSON_NAME_CONTENT)
        );
        sessionHandler.sendMessageToAll(msg, session);
    }
}
