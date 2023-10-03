package llq.fw.cm.dto;

import java.io.IOException;
import java.sql.Blob;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class BlobDeserializer extends JsonDeserializer<Blob> {

    private final SessionFactory sessionFactory;

    public BlobDeserializer(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Blob deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        byte[] bytes = jsonParser.getBinaryValue();
        try {
            return Hibernate.getLobCreator(sessionFactory.getCurrentSession()).createBlob(bytes);
        } catch (Exception e) {
            return null;
        }
    }
}




