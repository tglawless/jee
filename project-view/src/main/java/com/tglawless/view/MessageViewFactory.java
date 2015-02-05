package com.tglawless.view;

import java.util.List;

import com.tglawless.ejb.entities.MessageEntity;

public class MessageViewFactory {
	
	public static Message createMessage(MessageEntity entity){
		
		Message message = null;
		
		if(entity != null){
			message = new Message(
								entity.getId(),
								entity.getModifiedOn(),
								entity.getText());
		}
		
		return message;
	}
	
	public static Message[] createMessages(List<MessageEntity> entities){
		
		Message[] messages = new Message[entities.size()];
		
		if(entities != null && entities.size() > 0){
			for(int i = 0; i < entities.size(); i++){
				messages[i] = createMessage(entities.get(i));
			}
		}		
		
		return messages;
	}

}
