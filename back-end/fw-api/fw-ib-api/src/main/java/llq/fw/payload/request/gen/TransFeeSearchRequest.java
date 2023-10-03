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
public class TransFeeSearchRequest {
	private java.lang.Long id;
	private java.lang.Long beginAmount;
	private java.lang.Long beginNumber;
	private java.lang.String ccy;
	private java.lang.Long endAmount;
	private java.lang.Long endNumber;
	private java.lang.Long fee;
	private java.lang.Long feeMaxbal;
	private java.lang.Long feeMinbal;
	private java.lang.Long feePresent;
	private llq.fw.cm.enums.transfee.FeeTypeEnum feetype;
	private llq.fw.cm.enums.IBStatusEnum status;
	private java.lang.Long vat;
	private java.lang.String product;
}
