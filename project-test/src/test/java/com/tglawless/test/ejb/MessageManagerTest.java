package com.tglawless.test.ejb;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tglawless.ejb.InvalidMessageException;
import com.tglawless.ejb.MessageManager;
import com.tglawless.ejb.MessageNotFoundException;
import com.tglawless.ejb.entities.MessageEntity;

/**
 * The test cases for the <code>MessageManager</code> EJB.
 * 
 * @author tglawless
 *
 */
@RunWith(Arquillian.class)
public class MessageManagerTest {
	
	// Logger
	private static final Logger logger = Logger.getLogger(MessageManagerTest.class.getName());
	
	/**
	 * The injected EJB for testing.
	 */
	@Inject
	private MessageManager messageManager;
	
	/**
	 * The id of the test message created.
	 */
	private static long messageId;
	
	/**
	 * The text of the test message created.
	 */
	private static String messageText = "Test message at " + new Date();
	
	/**
	 * The date of the test message create.
	 */
	private static Date messageModifiedDate;
	
	/**
	 * Create the test WAR file targeted at the <code>MessageManager</code> EJB.
	 * 
	 * @return WebArchive - The WAR containing the code to be tested.
	 */
	@Deployment
	public static WebArchive createArchive(){
		
		logger.info("Creating test archive for deployment.");
		
		WebArchive war = ShrinkWrap.create(WebArchive.class);
		
		/*  This will add the classes in the same package as the MessageManager
		 *  as well as all of the classes in any / all sub-packages.
		 */		
		war.addPackages(true, MessageManager.class.getPackage());
		
		/* This will load the persistence XML from the classpath and add it
		 * to the test WAR is the correct location.  In this case, the 
		 * persistence.xml is coming from the EJB project.
		 */		
		war.addAsWebInfResource("META-INF/persistence.xml", "classes/META-INF/persistence.xml");

		/* This will load the web.xml from the classpath and add it
		 * to the test WAR is the correct location.  In this case, the 
		 * web.xml is coming from the test project.  This web.xml was created
		 * specifically for this test case and it is not the one to be used
		 * for deployment.
		 */		
		war.addAsWebInfResource("ejb-tests/WEB-INF/web.xml", "web.xml");

		/* This will create and empty beans.xml and add it to the test
		 * WAR so that CDI is enabled.
		 */		
		war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");	
		
		logger.finest("Test archive includes the following:");
		logger.finest(war.toString(true));
		
		return war;	
	}
	
	/**
	 * Test that the EJB was properly injected into the test case.
	 */
	@Test
	@InSequence(1)
	public void testMessageManagerInjection() {
		
		logger.finest("Executing test [Name: testMessageManagerInjection]");
		
		assertNotNull("The MessageManager was not injected into the test case.", messageManager);
	}

	/**
	 * Test that a message can not be created with invalid text.
	 */
	@Test
	@InSequence(2)
	public void testMessageManagerCreateMessageFail() {
		
		logger.finest("Executing test [Name: testMessageManagerCreateMessageFail]");

		try {
			messageManager.createMessage(null);
		} catch (InvalidMessageException e) {
			logger.log(Level.FINEST, e.getMessage(), e);
			return;
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			fail("The MessageManager did not throw the expected exception.");
		}
		
		fail("The MessageManager did not throw an exception as expected.");
	}

	/**
	 * Test that a message can be created successfully.
	 */
	@Test
	@InSequence(3)
	public void testMessageManagerCreateMessageSuccess() {
		
		logger.finest("Executing test [Name: testMessageManagerCreateMessageSuccess]");

		MessageEntity entity = null;
				
		try {
			entity = messageManager.createMessage(messageText);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			fail("The MessageManager throw an unexpected exception.");
		}
		
		logger.finest("Message created during test " + entity.toString());
		
		assertNotNull("The entity returned by MessageManager.createMessage() was null.", entity);
		assertNotNull("The entity was not assigned an id.", entity.getId());
		assertNotNull("The entity was not assigned a modified date.", entity.getModifiedOn());
		assertNotNull("The entity was not assigned provided text.", entity.getText());
		
		assertEquals("The text assigned to the entity does not match the text provided.",  messageText, entity.getText());
		
		messageId = entity.getId();
		messageModifiedDate = entity.getModifiedOn();
	}
	
	/**
	 * Test that the proper error is thrown when an invalid message ID is used
	 * when trying to locate a message.
	 */
	@Test
	@InSequence(4)
	public void testMessageManagerFindByIdFail() {
		
		logger.finest("Executing test [Name: testMessageManagerFindByIdFail]");

		try {
			messageManager.findMessageById(Integer.MAX_VALUE);
		} catch (MessageNotFoundException e){
			logger.log(Level.FINEST, e.getMessage(), e);
			return;
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			fail("The MessageManager did not throw the expected exception.");
		}
		
		fail("The MessageManager did not throw an exception as expected.");
	}

	/**
	 * Test that a message can be found using a valid ID.
	 */
	@Test
	@InSequence(5)
	public void testMessageManagerFindByIdSuccess() {
		
		logger.fine("Executing test [Name: testMessageManagerFindByIdSuccess]");
		
		MessageEntity entity = null;
		
		try {
			entity = messageManager.findMessageById(messageId);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			fail("The MessageManager throw an unexpected exception.");
		}
		
		assertNotNull("The entity is null, but it should not be.", entity);
		
		assertEquals("The id of the message does not match the expected value [Expected: " 
							+ messageId 
							+ "; Actual: " 
							+ entity.getId() 
							+ "]", 
					messageId, 
					entity.getId()); 
		
		assertEquals("The text of the message does not match the expected value [Expected: " 
							+ messageText
							+ "; Actual: " 
							+ entity.getText()
							+ "]", 
							messageText, 
					entity.getText()); 

	}

	/**
	 * Test that a message can not be updated using either an invalid message ID
	 * or invalid text.
	 */
	@Test
	@InSequence(6)
	public void testMessageManagerUpdateMessageFail() {
		
		logger.fine("Executing test [Name: testMessageManagerUpdateMessageFail]");
		
		try {
			messageManager.updateMessage(messageId, null);
		} catch (InvalidMessageException | MessageNotFoundException e) {
			logger.log(Level.FINEST, e.getMessage(), e);
			return;
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			fail("The MessageManager did not throw the expected exception.");
		}
		
		fail("The MessageManager did not throw an exception as expected.");
	}

	/**
	 * Test that a message can be updated with a valid message ID and text.
	 */
	@Test
	@InSequence(7)
	public void testMessageManagerUpdateMessageSuccess() {
		
		logger.fine("Executing test [Name: testMessageManagerUpdateMessageSuccess]");

		MessageEntity entity = null;
		messageText = "Message update at " + new Date();
		
		try {
			entity = messageManager.updateMessage(messageId, messageText);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			fail("The MessageManager throw an unexpected exception.");
		}
		
		assertNotNull("The entity returned by MessageManager.updateMessage() was null.", entity);
		assertNotNull("The entity was not assigned an id.", entity.getId());
		assertNotNull("The entity was not assigned a modified date.", entity.getModifiedOn());
		assertNotNull("The entity was not assigned provided text.", entity.getText());

		assertEquals("The id assigned to the entity does not match the id provided.",  messageId, entity.getId());
		assertEquals("The text assigned to the entity does not match the text provided.",  messageText, entity.getText());
		
		assertTrue("The modified date of the message was not updated properly [Original: " 
							+ messageModifiedDate 
							+ "; Modified: " 
							+ entity.getModifiedOn() 
							+ "]", 
					entity.getModifiedOn().after(messageModifiedDate)); 
		
	}

	/**
	 * Test that a message can not be delete with an invalid message ID;
	 */
	@Test
	@InSequence(8)
	public void testMessageManagerDeleteMessageFail() {

		logger.fine("Executing test [Name: testMessageManagerDeleteMessageFail]");

		try {
			messageManager.deleteMessage(Integer.MAX_VALUE);
		} catch (MessageNotFoundException e){
			logger.log(Level.FINEST, e.getMessage(), e);
			return;
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			fail("The MessageManager did not throw the expected exception.");
		}
		
		fail("The MessageManager did not throw an exception as expected.");
	}

	/**
	 * Test that a message can be deleted using a valid message ID.
	 */
	@Test
	@InSequence(9)
	public void testMessageManagerDeleteMessageSuccess() {

		logger.fine("Executing test [Name: testMessageManagerDeleteMessageSuccess]");

		try {
			messageManager.deleteMessage(messageId);
		} catch (MessageNotFoundException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			fail("The MessageManager throw an unexpected exception.");
		}
		
		try {
			messageManager.findMessageById(messageId);
		} catch (MessageNotFoundException e) {
			return;
		}
		
		fail("Message was not deleted as expected.");		
	}

	/**
	 * Test that a list of messages is sorted properly when returned from the EJB.
	 */
	@Test
	@InSequence(10)
	public void testMessageManagerFindAllMessages() {

		logger.fine("Executing test [Name: testMessageManagerFindAllMessages]");

		// Create some test messages
		int arraySize = 5;
		MessageEntity[] entities = new MessageEntity[arraySize];
		
		logger.finest("Creating test messages.");
		
		try {
			for(int i = 0; i < arraySize; i++){
				entities[i] = messageManager.createMessage("Message in array with index " + i);
			}
		} catch (InvalidMessageException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			fail("The MessageManager throw an unexpected exception.");
		}
		
		logger.finest("Quering test messages.");

		// Query the test messages
		List<MessageEntity> results = messageManager.findAllMessages();
		
		logger.finest("Confirming the messages are sorted properly.");

		// Make sure they are sorted in descending order.
		Date previousMessageDate = null;
		for(MessageEntity entity : results){
			if(previousMessageDate != null){
				assertTrue("THe list of messages are not sorted by modified date properly.", entity.getModifiedOn().before(previousMessageDate));
			}
			
			previousMessageDate = entity.getModifiedOn();
		}
		
		logger.finest("Cleaning up messages created during test.");

		// Delete the messages that were created for this test.
		try {
			for(MessageEntity entity : entities){
				messageManager.deleteMessage(entity.getId());
			}
		}catch (MessageNotFoundException e) {
			e.printStackTrace();
			fail("Error cleaning up after test case complete.");
		}
		
	}

}
