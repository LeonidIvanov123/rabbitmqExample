package ru.leonid.rabbitmqExample.RestController;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @GetMapping("/send")
    public String sendToRabbit(@RequestParam("msgtorabbit") String message){
        System.out.println("Из контроллера отправляем в очередь...");
        rabbitTemplate.convertAndSend("myTestQueue", message);
        return "the message is received to queue";
    }

    @GetMapping("/stat")
    public String testRabbitApp(){
        System.out.println("Application rabbitmqexample is UP");
        return "Application rabbitmqexample is UP";
    }
}
