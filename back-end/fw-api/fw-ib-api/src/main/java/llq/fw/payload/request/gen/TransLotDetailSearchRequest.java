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
public class TransLotDetailSearchRequest {
	private java.lang.Long id;
	private java.lang.String code;
	private java.lang.String amount;
	private java.lang.String approvedBy;
	private java.util.Date approvedDat;
	private java.lang.String ccy;
	private java.lang.String fee;
	private java.lang.String paymentStatus;
	private java.lang.String receiveAccount;
	private java.lang.String receiveBank;
	private java.lang.String receiveName;
	private java.lang.String reqId;
	private java.lang.String resCode;
	private java.lang.String resDes;
	private java.lang.String resId;
	private java.lang.String revertCode;
	private java.lang.String revertDes;
	private java.lang.String revertFeeCode;
	private java.lang.String revertFeeDes;
	private java.lang.String transDate;
	private java.util.Date transDatetime;
	private java.lang.String transTime;
	private java.lang.String txnum;
	private java.lang.String type;
	private java.lang.String vat;
	private String transLot;
}
