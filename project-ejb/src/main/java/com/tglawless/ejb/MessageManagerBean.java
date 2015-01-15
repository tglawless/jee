package com.tglawless.ejb;

import javax.ejb.Stateless;

@Stateless
public class MessageManagerBean implements MessageManager {
	
	@Override
	public String getMessage() {
		
		return "Hello EJB message!";
	}

}
