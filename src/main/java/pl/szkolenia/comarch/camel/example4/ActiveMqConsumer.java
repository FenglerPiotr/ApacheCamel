package pl.szkolenia.comarch.camel.example4;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

public class ActiveMqConsumer {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("activemq:queue:my_queue")
                        .to("seda:end");

            }
        });

        context.start();

        ConsumerTemplate consumerTemplate = context.createConsumerTemplate();
        /*System.out.println("Before receive");*/
        String message = consumerTemplate.receiveBody("seda:end", String.class);
        /*System.out.println("After receive");*/
        System.out.println(message);
    }
}
