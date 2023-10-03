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
public class BankReceivingSearchRequest {
	private java.lang.String code;
	private java.lang.String citad;
	private java.lang.String isttt;
	private java.lang.String name;
	private java.lang.String nameEn;
	private java.lang.String napas;
	private java.lang.String sortname;
	private java.lang.String status;
	private java.lang.String swiftCode;
}
