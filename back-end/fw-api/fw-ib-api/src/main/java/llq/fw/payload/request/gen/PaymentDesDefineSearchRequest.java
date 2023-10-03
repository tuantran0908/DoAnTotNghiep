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
public class PaymentDesDefineSearchRequest {
	private java.lang.Long id;
	private java.lang.String code;
	private java.lang.String content;
	private llq.fw.cm.enums.IBStatusEnum status;
}
