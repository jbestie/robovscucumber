package org.jbestie.robovscucumber.robot;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.util.ObjectHelper;
import org.junit.Test;

import java.util.List;

public class CallRobotTest extends CamelTestSupport {

    private Exchange exchange;

    @Override
    protected void doPostSetup() throws Exception {
        super.doPostSetup();
        exchange = createExchangeWithBody("Hello Robot");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateRobotVariablesFromCamelExchange() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);

        Exchange responseExchange = template.send("direct:setVariableCamelExchange", exchange);

        List<String> camelRobotVariables = ObjectHelper.cast(List.class, responseExchange.getIn().getHeader("CamelRobotVariables"));
        for (String camelRobotVariable : camelRobotVariables) {
            if (camelRobotVariable.contains("body")) {
                assertEquals("Body variable content should be [body:<body_value>]", "body:Hello Robot", camelRobotVariable);
            }
        }

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:setVariableCamelExchange")
                        .to("robotframework:src/test/resources/robot/camel_template.robot?xunitFile=target/out.xml&outputDirectory=target")
                        .to("mock:result");
            }
        };
    }
}