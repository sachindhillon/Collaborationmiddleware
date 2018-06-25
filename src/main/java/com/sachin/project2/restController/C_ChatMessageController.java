package com.sachin.project2.restController;

import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import com.sachin.project2.domain.Message;
import com.sachin.project2.domain.OutputMessage;

@RestController
public class C_ChatMessageController {
	@MessageMapping("/chat")
	@SendTo("/topic/message")
	public OutputMessage sendMessage(Message message)
	{
		return new OutputMessage(message,new Date());
	}

}
