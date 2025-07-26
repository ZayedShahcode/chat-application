package com.example.ChatApplication.chat;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")

    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage
    ){
        messagingTemplate.convertAndSend("/topic/room"+chatMessage.getRoomNumber(),chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ){
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("roomNumber",chatMessage.getRoomNumber());
        messagingTemplate.convertAndSend("/topic/room"+chatMessage.getRoomNumber(),chatMessage);
        chatMessage.setContent(chatMessage.getSender()+" has joined the room");
        return chatMessage;
    }
}
