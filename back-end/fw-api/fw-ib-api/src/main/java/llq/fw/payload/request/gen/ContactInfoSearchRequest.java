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
public class ContactInfoSearchRequest {
	private java.lang.String code;
	private java.lang.String address;
	private java.lang.String email;
	private java.lang.String fax;
	private java.lang.String hotline;
	private java.lang.String tel;
}
