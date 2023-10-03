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
public class CustSearchRequest {
	private java.lang.Long id;
	private java.util.Date birthday;
	private java.lang.String cif;
	private java.lang.String code;
	private java.lang.String email;
	private java.lang.String fullname;
	private java.lang.String idno;
	private llq.fw.cm.enums.cust.IndentifypapersEnum indentitypapers;
	private java.util.Date lastlogin;
	private java.lang.Long loginfalseNumber;
	private java.lang.String nmemonicName;
	private llq.fw.cm.enums.cust.NotificationEnum notification;
	private java.lang.String password;
	private llq.fw.cm.enums.cust.PositionEnum position;
	private llq.fw.cm.enums.cust.RoleSearchEnum rolesearch;
	private llq.fw.cm.enums.cust.RoleTransEnum roletrans;
	private java.lang.String sAllDd;
	private java.lang.String sAllFd;
	private java.lang.String sAllLn;
	private llq.fw.cm.enums.cust.SmsEnum sms;
	private llq.fw.cm.enums.cust.CustStatusEnum status;
	private java.lang.String tAllDd;
	private java.lang.String tel;
	private llq.fw.cm.enums.cust.VerifyTypeEnum verifyType;
	private java.util.Date approveAt;
	private java.lang.Long approveBy;
	private java.lang.String note;
	private String company;
	private java.util.Set custAcc;
	private java.util.Set custProduct;
	private java.util.Set passwordHi;
	private java.util.Set refreshtokenIbs;
	private java.util.Set transLimitCust;
}
