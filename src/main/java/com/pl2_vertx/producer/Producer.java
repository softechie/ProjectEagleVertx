package com.pl2_vertx.producer;

import com.pl2_vertx.dto.Log;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Producer {
    private KafkaProducer<String, JsonObject> producer;
    private String kafkaServer = "localhost:8000";
    //private String kafkaServer = "kafka-2ac2e185-antinywong-f578.aivencloud.com:14246";

    public Producer() {

        // Uncomment out this section and comment out next section if you are using LOCAL KAFKA SERVER WITHOUT SSL.
        

        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", kafkaServer);
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer");
        config.put("acks", "1");

        //Use producer for interacting with Apache Kafka
        producer = KafkaProducer.create(Vertx.currentContext().owner(), config);
    }
        

        //Uncomment this section out and comment out above section if using AIVEN KAFKA SERVER.
        /*try (InputStream input = new FileInputStream(new File("config.properties").getAbsolutePath())) {
            //Load props file content.
            Properties prop = new Properties();
            prop.load(input);

            //Extract props file content
            String trustStorePath = new File(prop.getProperty("kafka.truststore.path")).getAbsolutePath();
            String keyStorePath = new File(prop.getProperty("kafka.keystore.path")).getAbsolutePath();
            String trustStorePassword = prop.getProperty("kafka.truststore.password");
            String keyStorePassword = prop.getProperty("kafka.keystore.password");
            String producerFlag = prop.getProperty("producer.flag");

            //Set up configs for Kafka.
            Map<String, String> config = new HashMap<>();
            config.put("bootstrap.servers", kafkaServer);
            config.put("security.protocol", "SSL");
            config.put("ssl.truststore.location", trustStorePath);
            config.put("ssl.truststore.password", trustStorePassword);
            config.put("ssl.keystore.type", "PKCS12");
            config.put("ssl.keystore.location", keyStorePath);
            config.put("ssl.keystore.password", keyStorePassword);
            config.put("ssl.key.password", keyStorePassword);
            config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            config.put("value.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer");
            config.put("acks", "1");
            //Use producer for interacting with Apache Kafka
            if(producerFlag.equals("1"))
                producer = KafkaProducer.create(Vertx.currentContext().owner() , config);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
*/
    public void sendLog(Log log){
        if(producer != null) {
            KafkaProducerRecord<String, JsonObject> record =
                    KafkaProducerRecord.create("Database", log.getLogId(),
                            JsonObject.mapFrom(log));

            producer.write(record, done -> {
                if (done.succeeded())
                    System.out.println("Log Sent: " + log.getLogId());
                else
                    System.out.println("Fail " + done.cause());
            });
        } else {
            System.out.println("Logging has been turned off.");
        }
    }
}

