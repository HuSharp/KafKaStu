package chapter02;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class StringSerializer implements Serializer<String> {
    private String encoding = "UTF-8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String propertyName = isKey ? "key.serializer.encoding" : "value.serializer.encoding";
        Object encodingVal = configs.get(propertyName);
        if (encodingVal == null) {
            encodingVal = configs.get("serializer.encoding");
        }
        if (encodingVal instanceof String) {
            encoding = (String) encodingVal;
        }
    }

    // 将 String 类型转化为 byte[]
    @Override
    public byte[] serialize(String topic, String data) {
        try {
            if (data == null) {
                return null;
            } else {
                return data.getBytes(encoding);
            }
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("Error when serializing");
        }
    }

    @Override
    public void close() {

    }
}
