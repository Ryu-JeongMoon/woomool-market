package com.woomoolmarket.chat.controller;

import com.woomoolmarket.chat.model.ChatMessage;
import com.woomoolmarket.domain.entity.Member;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ChatController {

  @GetMapping("/chat")
  public String chat(HttpServletRequest request, Model model) {
    HttpSession session = request.getSession();
    Member loginMember = (Member) session.getAttribute("loginMember");
    model.addAttribute("loginId", loginMember.getId());
    return "chat";
  }

  @MessageMapping("/chat.sendMessage")
  @SendTo("/topic/public")
  public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
    log.info("chatSender = {}, chatMessage = {}", chatMessage.getSender(), chatMessage.getContent());
    return chatMessage;
  }

  @MessageMapping("/chat.addUser")
  @SendTo("/topic/public")
  public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
    headerAccessor.getSessionAttributes()
      .put("username", chatMessage.getSender());
    return chatMessage;
  }
}
