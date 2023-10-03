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
public class TransLimitCustSearchRequest {
	private java.lang.Long id;
	private java.lang.Long maxdaily;
	private java.lang.Long maxtrans;
	private java.lang.String product;
	private java.lang.String productName;
	private String cust;
}
