package chapter03;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class KafkaConsumerAnalysis {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-demo";
    public static final String groupId = "group.demo";
    public static final AtomicBoolean isRunning = new AtomicBoolean(true);

    public static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);    // 消费者隶属的消费组
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "consumer.client.id.demo");
        properties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, ConsumerInterceptorTTL.class.getName());
        return properties;
    }

    public static void main(String[] args) {
        Properties properties = initConfig();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        // 订阅主题 topic 两种方式： subscribe 和 assign
        consumer.subscribe(Arrays.asList(topic));

//        //  consumer.assign(Arrays.asList(new TopicPartition(topic, 0)));
//        //  获取指定主题的分区元数据信息
//        List<TopicPartition> partitions = new ArrayList<>();
//        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
//        if (partitionInfos != null) {
//            for (PartitionInfo partitionInfo : partitionInfos) {
//                partitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
//            }
//        }
//        consumer.assign(partitions);

        /* 反订阅
        consumer.unsubscribe();
        consumer.assign(new ArrayList<TopicPartition>());
        consumer.subscribe(new ArrayList<String>());
        */


        try {
            while (isRunning.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("topic = " + record.topic()
                            + ", partition = " + record.partition()
                            + ", offset = " + record.offset());
                    System.out.println("key = " + record.key()
                            + ", value = " + record.value());
                }
                /*// 分区为维度
                for (TopicPartition tp : records.partitions()) {
                    // public List<ConsumerRecord<K, V>> records(TopicPartition partition)
                    // 获取 records 中指定分区的所有记录
                    for (ConsumerRecord<String, String> record : records.records(tp)) {

                    }
                }*/

                //do something to process record.
            }
        } catch (Exception e) {
            log.error("occur exception ", e);
        } finally {
            consumer.close();
        }
    }
}
