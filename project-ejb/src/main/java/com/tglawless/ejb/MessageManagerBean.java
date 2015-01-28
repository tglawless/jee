package com.tglawless.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.tglawless.ejb.entities.MessageEntity;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class MessageManagerBean implements MessageManager {
	
	@PersistenceContext(unitName="messages")
	private EntityManager em;
	
	@Inject
	private MessageManager messageManager;

	@Override
	public List<MessageEntity> findAllMessages() {
		
		TypedQuery<MessageEntity> query = em.createNamedQuery(MessageEntity.FIND_ALL_QUERY, MessageEntity.class);
		return query.getResultList();		
	}

	@Override
	public MessageEntity findMessageById(long id) throws MessageNotFoundException {

		MessageEntity entity = em.find(MessageEntity.class, id);
		
		if(entity == null){
			throw new MessageNotFoundException("Message not found. [ID: " + id + "]");
		}
		
		return entity;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MessageEntity createMessage(String text) throws InvalidMessageException {

		if(text == null || text.length() < 1){
			throw new InvalidMessageException("The text of the message can not be null and must have at least one character.");
		}
		
		MessageEntity entity = new MessageEntity(text);
		entity = em.merge(entity);
		
		return entity;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MessageEntity updateMessage(long id, String text) throws MessageNotFoundException, InvalidMessageException {
		
		if(text == null || text.length() < 1){
			throw new InvalidMessageException("The text of the message can not be null and must have at least one character.");
		}
		
		MessageEntity entity = messageManager.findMessageById(id);
		entity.setText(text);
		
		entity = em.merge(entity);
		
		return entity;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteMessage(long id) throws MessageNotFoundException {
		
		MessageEntity entity = messageManager.findMessageById(id);
		
		em.remove(entity);		
	}
	
}
