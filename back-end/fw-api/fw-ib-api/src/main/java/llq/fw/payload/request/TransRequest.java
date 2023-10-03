package llq.fw.payload.request;

import llq.fw.cm.models.Cust;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TransRequest {

    String code;
    private String amount;
    private java.util.Date approvedDate;
    private Cust approvedBy;
    private String branch;
    private String ccy;
    private String content;
    private Long custId;
    private String fee;
    private String feeType;
    private String paymentStatus;
    private String receiveAccount;
    private String receiveBank;
    private String receiveName;
    private String reqId;
    private String resCode;
    private String resDes;
    private String resId;
    private String revertCode;
    private String revertDes;
    private String revertFeeCode;
    private String revertFeeDes;
    private String sendAccount;
    private String sendName;
    private String status;
    private String transDate;
    private java.util.Date transDatetime;
    private String transTime;
    private String txnum;
    private String type;
    private String username;
    private String vat;
    private String schedules;
    private java.util.Date scheduleFuture;
    private java.util.Date schedulesFromDate;
    private java.util.Date schedulesToDate;
    private String schedulesFrequency;
    private long schedulesTimes;
    private String schedulesTimesFrequency;
}
