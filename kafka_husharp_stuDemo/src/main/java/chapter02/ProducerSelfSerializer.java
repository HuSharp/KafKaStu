package chapter02;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerSelfSerializer {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-demo";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        properties.put("bootstrap.servers", brokerList);

//        KafkaProducer<String, Company> producer1 = new KafkaProducer<String, Company>(properties);
//        Company company = Company.builder().name("hiddenkafka").address("China").build();
//        ProducerRecord<String, Company> record = new ProducerRecord<>(topic, company);
//        producer1.send(record).get();

        KafkaProducer<String, String> producer2 = new KafkaProducer<String, String>(properties);
        String str = "hello, kafka!";
        ProducerRecord<String, String> record2 = new ProducerRecord<>(topic, str);
        producer2.send(record2).get();
    }
}
