package com.tglawless.test.rest;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Date;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.RestClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tglawless.ejb.MessageManager;
import com.tglawless.rest.MessageResource;
import com.tglawless.view.Message;

@RunWith(Arquillian.class)
public class MessageResourceTest {
	
	// Logger
	private static final Logger logger = Logger.getLogger(MessageResourceTest.class.getName());
	
	private static String contextRoot = "message_test";
	
	private static String testMessage = "This is a test message.";
	
	private static long messageId;

	@Deployment
	public static WebArchive createArchive(){
		
		logger.info("Creating test archive for deployment.");
		
		WebArchive war = ShrinkWrap.create(WebArchive.class, contextRoot + ".war");
		
		/*  This will add the classes in the same package as the MessageManager
		 *  as well as all of the classes in any / all sub-packages.
		 */		
		war.addPackages(true, MessageManager.class.getPackage(), 
								MessageResource.class.getPackage(), 
								Message.class.getPackage());
		
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
		
		File wlp_jaxrs = Maven.resolver().resolve("com.ibm.websphere.appserver.thirdparty:com.ibm.websphere.appserver.thirdparty.jaxrs:1.0.3").withoutTransitivity().asSingleFile();
		File sl4j = Maven.resolver().resolve("org.slf4j:slf4j-api:1.7.10").withoutTransitivity().asSingleFile();
		
		war.addAsLibraries(wlp_jaxrs, sl4j);
		
		logger.finest("Test archive includes the following:");
		logger.finest(war.toString(true));
		
		return war;	
	}
	
	@Test
	@InSequence(1)
	public void testCreateMessage(){
		
		final String testUrl = String.format("http://localhost:9080/%s/messages", contextRoot);
		
		RestClient client = new RestClient();
		Message message = new Message();
		message.setValue(testMessage);
		
		ClientResponse response = client.resource(testUrl)
									.accept(MediaType.APPLICATION_JSON)
									.contentType(MediaType.APPLICATION_JSON)
									.post(message);	
		
		assertEquals("The status code of the response is not the expected value.", Status.CREATED.getStatusCode(), response.getStatusCode());

		Message respMsg = response.getEntity(Message.class);
		
		assertNotNull("The message id was not set by the service.", respMsg.getId());
		
		messageId = respMsg.getId();
		
		assertNotNull("The message modified date was not set by the service.", respMsg.getLastUpdated());
		assertEquals("The value of the message does not match what was provided.",  message.getValue(), respMsg.getValue());
	}
	
	@Test
	@InSequence(2)
	public void testFindMessageById(){
		
		final String testUrl = String.format("http://localhost:9080/%s/messages/%d", contextRoot, messageId);
		
		RestClient client = new RestClient();
		ClientResponse response = client.resource(testUrl)
									.accept(MediaType.APPLICATION_JSON)
									.get();	
		
		assertEquals("The status code of the response is not the expected value.", Status.OK.getStatusCode(), response.getStatusCode());

		Message respMsg = response.getEntity(Message.class);
		assertNotNull("The message id was not set by the service.", respMsg.getId());
		assertNotNull("The message modified date was not set by the service.", respMsg.getLastUpdated());
		assertEquals("The value of the message does not match what was provided.",  testMessage, respMsg.getValue());
	}
	
	@Test
	@InSequence(3)
	public void testUpdateMessage(){
		
		final String testUrl = String.format("http://localhost:9080/%s/messages/%d", contextRoot, messageId);
		
		RestClient client = new RestClient();
		Message message = new Message();
		message.setValue(testMessage + " - updated: " + new Date());
		
		ClientResponse response = client.resource(testUrl)
									.accept(MediaType.APPLICATION_JSON)
									.contentType(MediaType.APPLICATION_JSON)
									.put(message);	
		
		assertEquals("The status code of the response is not the expected value.", Status.OK.getStatusCode(), response.getStatusCode());

		Message respMsg = response.getEntity(Message.class);
		assertNotNull("The message id was not set by the service.", respMsg.getId());
		assertNotNull("The message modified date was not set by the service.", respMsg.getLastUpdated());
		assertEquals("The value of the message does not match what was provided.",  message.getValue(), respMsg.getValue());
	}
	
	@Test
	@InSequence(4)
	public void testFindAllMessages(){
		
		final String testUrl = String.format("http://localhost:9080/%s/messages", contextRoot);
		
		RestClient client = new RestClient();
		ClientResponse response = client.resource(testUrl)
								.accept(MediaType.APPLICATION_JSON)
								.get();
		
		Message[] messages = response.getEntity(Message[].class);
		
		assertNotNull("The list of messages returned by the service was null.", messages);
		assertTrue("Test expected at least 1 message to be contained within the list.", messages.length > 0);
	}

	@Test
	@InSequence(5)
	public void testDeletelMessage(){
		
		final String testUrl = String.format("http://localhost:9080/%s/messages/%d", contextRoot, messageId);
		
		RestClient client = new RestClient();
		ClientResponse response = client.resource(testUrl)
								.accept(MediaType.APPLICATION_JSON)
								.delete();
		
		assertEquals("The status code of the response is not the expected value.", Status.OK.getStatusCode(), response.getStatusCode());
	}

}
