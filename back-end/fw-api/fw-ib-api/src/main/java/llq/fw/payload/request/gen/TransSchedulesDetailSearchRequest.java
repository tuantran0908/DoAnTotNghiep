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
public class TransSchedulesDetailSearchRequest {
	private java.lang.Long id;
	private java.lang.String code;
	private java.lang.String paymentStatus;
	private java.lang.String reqId;
	private java.lang.String resCode;
	private java.lang.String resDes;
	private java.lang.String resId;
	private java.lang.String revertCode;
	private java.lang.String revertDes;
	private java.lang.String revertFeeCode;
	private java.lang.String revertFeeDes;
	private java.util.Date scheduledDate;
	private java.util.Date transDate;
	private String transSchedule;
}
