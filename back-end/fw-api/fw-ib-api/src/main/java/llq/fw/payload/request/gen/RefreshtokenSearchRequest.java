package llq.fw.payload.request.gen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshtokenSearchRequest {
	private java.lang.Long id;
	private java.time.Instant expiryDate;
	private java.lang.String token;
	private String user;
}
