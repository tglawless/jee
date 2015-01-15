package com.tglawless.test.ejb;

import static org.junit.Assert.*;

import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import com.tglawless.ejb.MessageManager;

public class MessageManagerSimpleTest {
	
	private EJBContainer container;
	
	@Inject
	private MessageManager messageManager;
	
	@Before
    public void setUp() throws Exception {
		container = EJBContainer.createEJBContainer();
		container.getContext().bind("inject", this);
    }
	
	public void tearDown() throws Exception {
		container.close();
	}

	@Test
	public void getMessage() {
		assertNotNull("CDI injection was unsuccessful.", messageManager);
		assertEquals("Message from EJB did not match expected.", "Hello EJB message!", messageManager.getMessage());
	}

}
