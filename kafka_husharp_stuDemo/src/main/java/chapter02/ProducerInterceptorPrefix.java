package chapter02;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class ProducerInterceptorPrefix implements ProducerInterceptor<String, String> {
    private volatile long sendSuccess = 0;
    private volatile long sendFailure = 0;

    // 为每条消息加上 ”prefix1-" 的前缀
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        String modifiedVal = "prefix1-" + record.value();
        return new ProducerRecord<>(record.topic(), record.partition(),
                record.timestamp(), record.key(), modifiedVal, record.headers());
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if (e == null) {
            sendSuccess++;
        } else {
            sendFailure++;
        }
    }

    @Override
    public void close() {
        double successRatio  = (double)(sendSuccess / (sendFailure + sendSuccess));
        System.out.println("[INFO] success ratio is: " +
                String.format("%f", successRatio));
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
