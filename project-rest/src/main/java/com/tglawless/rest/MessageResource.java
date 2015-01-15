package com.tglawless.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tglawless.ejb.MessageManager;

@Path("/message")
public class MessageResource {
	
	@Inject
	private MessageManager messageManager;

	@GET
	public Response getMessage(){
		
		return Response.ok(messageManager.getMessage()).type(MediaType.TEXT_PLAIN).build();
	}
}
