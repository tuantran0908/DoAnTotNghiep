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
public class PackageFeeSearchRequest {
	private java.lang.Long id;
	private java.lang.String day;
	private java.lang.Long fee;
	private java.lang.Long feeReg;
	private llq.fw.cm.enums.packagefee.FreeEnum free;
	private llq.fw.cm.enums.packagefee.IntFeeMethodEnum intfeemethod;
	private java.math.BigDecimal minbalDay;
	private java.math.BigDecimal minbalMonth;
	private java.math.BigDecimal numberMonth;
	private String custType;
	private String packageCode;
	private java.util.Set packageFeeCts;
}
