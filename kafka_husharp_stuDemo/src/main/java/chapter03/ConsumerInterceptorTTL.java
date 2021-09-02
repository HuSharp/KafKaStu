package chapter03;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 实现一个消息过期 TTL
public class ConsumerInterceptorTTL implements ConsumerInterceptor<String, String> {

    private static final long EXPIRE_INTERVAL = 10 * 1000;

    // 在 poll 返回之前调用此方法

    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> consumerRecords) {
        System.out.println("before: " + consumerRecords);
        long now = System.currentTimeMillis();

        Map<TopicPartition, List<ConsumerRecord<String, String>>> retRecords = new HashMap<>();
        for (TopicPartition tp : consumerRecords.partitions()) {
            List<ConsumerRecord<String, String>> tpRecords = consumerRecords.records(tp);
            List<ConsumerRecord<String, String>> retTpRecords = new ArrayList<>();
            for (ConsumerRecord<String, String> record : tpRecords) {
                if (now - record.timestamp() < EXPIRE_INTERVAL) {
                    retTpRecords.add(record);
                }
            }
            if (!retTpRecords.isEmpty()) {
                retRecords.put(tp, retTpRecords);
            }

        }
        return new ConsumerRecords<>(retRecords);
    }

    @Override
    public void close() {

    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
        offsets.forEach((tp, offset) ->
                System.out.println("[onCommit]" + tp + ":" + offset.offset()));
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
