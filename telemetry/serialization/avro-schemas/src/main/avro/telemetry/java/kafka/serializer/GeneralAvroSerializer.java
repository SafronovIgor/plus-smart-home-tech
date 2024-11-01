package kafka.serializer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class GeneralAvroSerializer implements Serializer<SpecificRecordBase> {
    private final EncoderFactory encoderFactory = EncoderFactory.get();

    @Override
    public byte[] serialize(String topic, SpecificRecordBase data) {
        if (data == null) {
            log.warn("Null data provided for topic: {}", topic);
            return new byte[0];
        }

        try (var byteArrayOutputStream = new ByteArrayOutputStream()) {
            var encoder = encoderFactory.binaryEncoder(byteArrayOutputStream, null);
            var writer = new SpecificDatumWriter<>(data.getSchema());

            writer.write(data, encoder);
            encoder.flush();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("Error serializing Avro data", e);
            throw new SerializationException("Error serializing Avro data", e);
        }
    }
}