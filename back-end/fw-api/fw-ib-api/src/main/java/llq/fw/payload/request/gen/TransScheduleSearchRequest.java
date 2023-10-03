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
public class TransScheduleSearchRequest {
	private java.lang.String code;
	private java.lang.String amount;
	private java.lang.String approvedBy;
	private java.util.Date approvedDate;
	private java.lang.String branch;
	private java.lang.String ccy;
	private java.lang.String content;
	private java.lang.String custId;
	private java.lang.String fee;
	private java.lang.String receiveAccount;
	private java.lang.String receiveBank;
	private java.lang.String receiveName;
	private java.lang.String sendAccount;
	private java.lang.String sendName;
	private java.lang.String status;
	private java.lang.String transDate;
	private java.util.Date transDatetime;
	private java.lang.String transTime;
	private java.lang.String txnum;
	private java.lang.String type;
	private java.lang.String username;
	private java.lang.String vat;
	private llq.fw.cm.enums.trans.SchedulesEnum schedules;
	private java.util.Set transSchedulesDetails;
}
