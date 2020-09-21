package org.jbestie.robovscucumber.blueprint;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;

import javax.jms.*;

@Slf4j
public class TestBlueprintRoute extends CamelBlueprintTestSupport {

    private static final String SOURCE_ENDPOINT = "activemq:";
    private static final String VALID_QUEUE = "valid";
    private static final String DESTINATION_HEADER = "destination";

    private Connection connection;
    private Session session;

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/camelContext.xml";
    }

    public void sendActiveMqMessage(String queueName, String message) throws Exception {
        this.setUp();
        setUpConnection();
        template.sendBodyAndHeader(SOURCE_ENDPOINT + queueName, message, DESTINATION_HEADER, VALID_QUEUE);
    }

    public void testRouteWithValidMessage(String queueName, String expectedMessage) throws Exception {
        try {
            // assert mocks
            assertMockEndpointsSatisfied();

            Message message = readMessageFromQueue(queueName);
            assertNotNull("Should have received a message from destination: " + queueName, message);

            TextMessage textMessage = assertIsInstanceOf(TextMessage.class, message);
            assertEquals("Message body", expectedMessage, textMessage.getText());
            log.error("Received from " + queueName + " is " + textMessage);
        } finally {
            tearDownConnection();
        }
    }

    private Message readMessageFromQueue(String queueName) throws Exception {
        MessageConsumer consumer = session.createConsumer(session.createQueue(queueName));
        try {
            return consumer.receive(1000);
        } finally {
            consumer.close();
        }
    }

    public void setUpConnection() throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void tearDownConnection() throws Exception {
        session.close();
        connection.close();
        connection.close();
    }
}
