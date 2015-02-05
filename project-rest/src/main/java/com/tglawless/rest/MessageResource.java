package com.tglawless.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.tglawless.ejb.InvalidMessageException;
import com.tglawless.ejb.MessageManager;
import com.tglawless.ejb.MessageNotFoundException;
import com.tglawless.ejb.entities.MessageEntity;
import com.tglawless.view.Message;
import com.tglawless.view.MessageViewFactory;

@Path("/messages")
public class MessageResource {
	
	@Inject
	private MessageManager messageManager;

	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response findAllMessages(){
		
		List<MessageEntity> entities = messageManager.findAllMessages();
		
		return Response.ok(MessageViewFactory.createMessages(entities))
						.build();
	}
	
	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response findMessageById(@PathParam("id") long id) throws MessageNotFoundException{
		
		MessageEntity entity = messageManager.findMessageById(id);
		
		return Response.ok(MessageViewFactory.createMessage(entity)).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response createMessage(Message message) throws InvalidMessageException{
		
		MessageEntity entity = messageManager.createMessage(message.getValue());
		
		return Response.status(Status.CREATED)
					.entity(MessageViewFactory.createMessage(entity))
					.build();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updateMessage(@PathParam("id") long id, Message message) throws MessageNotFoundException, InvalidMessageException {
		
		MessageEntity entity = messageManager.updateMessage(id, message.getValue());
		
		return Response.status(Status.OK)
				.entity(MessageViewFactory.createMessage(entity))
				.build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteMessage(@PathParam("id") long id) throws MessageNotFoundException{
		
		messageManager.deleteMessage(id);
		
		return Response.ok().build();
	}
	
}
