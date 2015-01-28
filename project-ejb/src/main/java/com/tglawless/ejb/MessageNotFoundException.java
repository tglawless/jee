package com.tglawless.ejb;

@SuppressWarnings("serial")
public class MessageNotFoundException extends Exception {

	public MessageNotFoundException() {
		super();
	}

	public MessageNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MessageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageNotFoundException(String message) {
		super(message);
	}

	public MessageNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
