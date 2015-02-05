package com.tglawless.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.tglawless.ejb.MessageNotFoundException;

@Provider
public class MessageNotFoundExceptionMapper implements
		ExceptionMapper<MessageNotFoundException> {

	@Override
	public Response toResponse(MessageNotFoundException e) {
		
		return Response.status(Status.NOT_FOUND)
						.entity(e.getMessage())
						.build();
	}

}
