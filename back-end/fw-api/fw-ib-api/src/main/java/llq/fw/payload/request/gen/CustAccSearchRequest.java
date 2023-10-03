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
public class CustAccSearchRequest {
	private java.lang.Long id;
	private java.lang.String acc;
	private java.lang.String type;
	private java.lang.String accType;
	private String cust;
}
