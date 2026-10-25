package com.ticketrush.inventory_service.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;

public class MessageSender {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper;

    public MessageSender(KafkaTemplate<String, String> kafka, ObjectMapper mapper) {
        this.kafka = kafka;
        this.mapper = mapper;
    }

    public void send(String topic , Message message)
    {

        try {
            String json = mapper.writeValueAsString(message);
            kafka.send(topic, message.bookingId(), json)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            logger.error("** Msg could not be send! msg failed : {} : {} " , message.toString() , ex.getMessage() );
                        }
                    });
        }
        catch(JacksonException  e)
        {
            throw new IllegalStateException(e);
        }
    }

}
