package org.jbestie.robovscucumber.blueprint;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;

public class OrdinalBlueprintTest extends CamelBlueprintTestSupport {

    private static final String SOURCE_ENDPOINT = "activemq:testqueue";
    private static final String VALID_QUEUE = "valid";
    private static final String INVALID_ENDPOINT = "mock:invalid";
    private static final String DESTINATION_HEADER = "destination";

    private Connection connection;
    private Session session;

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/camelContext.xml";
    }

    @Before
    public void setUpConnection() throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @After
    public void tearDownConnection() throws Exception {
        session.close();
        connection.close();
        connection.close();
    }

    @Test
    public void testRouteWithValidMessage() throws Exception {
        // set mock expectations
        getMockEndpoint(INVALID_ENDPOINT).expectedMessageCount(0);

        // send a message
        template.sendBodyAndHeader(SOURCE_ENDPOINT, "Welcome to RoboHell!", DESTINATION_HEADER, VALID_QUEUE);

        // assert mocks
        assertMockEndpointsSatisfied();

        Message message = readMessageFromQueue();
        assertNotNull("Should have received a message from destination: " + VALID_QUEUE, message);

        TextMessage textMessage = assertIsInstanceOf(TextMessage.class, message);
        assertEquals("Message body", "Welcome to RoboHell!", textMessage.getText());
    }

    private Message readMessageFromQueue() throws Exception {
        MessageConsumer consumer = session.createConsumer(session.createQueue(OrdinalBlueprintTest.VALID_QUEUE));
        try {
            return consumer.receive(1000);
        } finally {
            consumer.close();
        }
    }

}
