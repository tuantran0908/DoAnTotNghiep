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
public class TransLotSearchRequest {
	private java.lang.String code;
	private java.lang.String amount;
	private java.lang.String approvedBy;
	private java.util.Date approvedDate;
	private java.lang.String branch;
	private java.lang.String content;
	private java.lang.String custId;
	private java.lang.String fee;
	private java.lang.String sendAccount;
	private java.lang.String status;
	private java.lang.String total;
	private java.lang.String transDate;
	private java.util.Date transDatetime;
	private java.lang.String transTime;
	private java.lang.String username;
	private java.lang.String vat;
	private java.util.Set transLotDetails;
}
