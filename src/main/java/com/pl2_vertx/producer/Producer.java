package com.pl2_vertx.producer;

import com.pl2_vertx.dto.Log;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import java.util.HashMap;
import java.util.Map;

public class Producer {
    private KafkaProducer<String, JsonObject> producer;
    //private String kafkaServer = "localhost:9092";
    private String kafkaServer = "kafka-2ac2e185-antinywong-f578.aivencloud.com:14246";

    public Producer() {
        Vertx vertx = Vertx.vertx();
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", kafkaServer);
        config.put("security.protocol", "SSL");
        config.put("ssl.truststore.location", "C:\\Users\\trana\\Desktop\\keys\\client.truststore.jks");
        config.put("ssl.truststore.password", "password");
        config.put("ssl.keystore.type", "PKCS12");
        config.put("ssl.keystore.location", "C:\\Users\\trana\\Desktop\\keys\\client.keystore.p12");
        config.put("ssl.keystore.password", "password");
        config.put("ssl.key.password", "password");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer");
        config.put("acks", "1");

        // use producer for interacting with Apache Kafka
        producer = KafkaProducer.create(vertx, config);
    }

    public void sendLog(Log log){
        KafkaProducerRecord<String, JsonObject> record =
                KafkaProducerRecord.create("Database", log.getLogId(),
                        JsonObject.mapFrom(log));

        producer.write(record, done -> {
            if(done.succeeded())
                System.out.println("Log Sent: " + log.getLogId());
            else
                System.out.println("Fail " + done.cause());
        });
    }
}
