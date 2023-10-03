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
public class TransLimitDetailSearchRequest {
	private java.lang.Long id;
	private java.math.BigDecimal maxdaily;
	private java.math.BigDecimal maxmonth;
	private java.lang.Long maxtrans;
	private java.lang.String product;
	private java.lang.String productName;
	private java.math.BigDecimal totaldaily;
	private String transLimit;
}
