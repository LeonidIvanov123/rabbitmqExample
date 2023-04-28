package ru.leonid.rabbitmqExample;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${rabbit.server}")
    private String rabbitserver;

    @Bean
    public CachingConnectionFactory connectionFactory(){
        System.out.println("\nConnect to RabbitMQ server: " + rabbitserver + "\n");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitserver);
        return connectionFactory;
    }
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    //объявляем очереди
    @Bean
    public Queue myQueue1() {
        return new Queue("myTestQueue");
    }

    @Bean
    public Queue logQueue(){
        return new Queue("myLogQueue");
    }

    //создали обменник сообщениями
    @Bean
    public FanoutExchange fanoutExchange1(){
        return new FanoutExchange("myExchange");
        //FanOut - Все сообщения во все очереди, самый быстрый тк не использует ключи маршрутизации
    }
    //биндим обе очереди к обменнику
    @Bean
    public Binding binding1(){
        return BindingBuilder.bind(myQueue1()).to(fanoutExchange1());
    }
    @Bean
    public Binding binding2(){
        return BindingBuilder.bind(logQueue()).to(fanoutExchange1());
    }

    //слушатель очереди
    @Bean
    public SimpleMessageListenerContainer messageListenerContainer1() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames("myTestQueue");
        container.setMessageListener(new MessageListener() {
            //тут ловим сообщения из queue1
            public void onMessage(Message message) {
                System.out.println("received from queue1 : " + new String(message.getBody()));
            }
        });
        return container;
    }

    @RabbitListener(queues = "myLogQueue")
    public void readLogQueue(final Message message){
        System.out.println("msg from myLogQueue" + new String(message.getBody()));
    }

}
