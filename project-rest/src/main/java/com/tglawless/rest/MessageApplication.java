package com.tglawless.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/messages")
public class MessageApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		
		Set<Class<?>> clss = new HashSet<Class<?>>();
		clss.add(MessageResource.class);
		
		return clss;
	}
}
