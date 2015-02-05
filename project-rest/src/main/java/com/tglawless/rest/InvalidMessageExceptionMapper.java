package com.tglawless.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.tglawless.ejb.InvalidMessageException;

@Provider
public class InvalidMessageExceptionMapper implements
		ExceptionMapper<InvalidMessageException> {

	@Override
	public Response toResponse(InvalidMessageException e) {
		
		return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
	}

}
