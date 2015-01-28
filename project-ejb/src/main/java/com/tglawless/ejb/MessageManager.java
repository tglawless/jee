package com.tglawless.ejb;

import java.util.List;

import javax.ejb.Local;

import com.tglawless.ejb.entities.MessageEntity;

@Local
public interface MessageManager {
	
	public List<MessageEntity> findAllMessages();
	
	public MessageEntity findMessageById(long id) throws MessageNotFoundException;
	
	public MessageEntity createMessage(String text) throws InvalidMessageException;
	
	public MessageEntity updateMessage(long id, String text) throws MessageNotFoundException, InvalidMessageException;
	
	public void deleteMessage(long id) throws MessageNotFoundException;
	
}
