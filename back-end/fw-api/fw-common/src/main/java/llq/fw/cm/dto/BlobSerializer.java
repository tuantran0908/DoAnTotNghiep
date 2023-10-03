package llq.fw.cm.dto;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class BlobSerializer extends JsonSerializer<Blob> {

    @Override
    public void serialize(Blob value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try (InputStream is = value.getBinaryStream()) {
            byte[] bytes = is.readAllBytes();
            String base64String = Base64.getEncoder().encodeToString(bytes);
            gen.writeString(base64String);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
     

	
}

