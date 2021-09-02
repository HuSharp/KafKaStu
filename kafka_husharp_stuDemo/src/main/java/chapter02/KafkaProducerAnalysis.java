package chapter02;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducerAnalysis {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-demo";

    public static Properties initConfig() {
        Properties properties = new Properties();
        // 指定发送信息的容器
        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("bootstrap.servers", brokerList);
        properties.put("client.id", "producer.client.id.demo");
        return properties;
    }

    public static Properties initNewConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer.client.id.demo");
        return props;
    }

    public static void main(String[] args) {
        Properties properties = initConfig();
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "Hello kafka!");
        try {
            producer.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2021.09.02 测试 Consumer 的超时拦截器
        try {
            final long EXPIRE_INTERVAL = 10 * 1000;
            ProducerRecord<String, String> record1 = new ProducerRecord<>(
                    topic, 0, System.currentTimeMillis() - EXPIRE_INTERVAL, null, "first-expire-data!");
            producer.send(record1);
            ProducerRecord<String, String> record2 = new ProducerRecord<>(
                    topic, 0, System.currentTimeMillis() - EXPIRE_INTERVAL, null, "normal-data!");
            producer.send(record2);
            ProducerRecord<String, String> record3 = new ProducerRecord<>(
                    topic, 0, System.currentTimeMillis() - EXPIRE_INTERVAL, null, "last-expire-data!");
            producer.send(record3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
