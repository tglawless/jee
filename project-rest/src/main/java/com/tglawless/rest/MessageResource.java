package com.tglawless.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/message")
public class MessageResource {

	@GET
	public Response getMessage(){
		
		return Response.ok("Hello message!").type(MediaType.TEXT_PLAIN).build();
	}
}
