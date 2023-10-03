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
public class CustContactSearchRequest {
	private java.lang.Long id;
	private java.math.BigDecimal custId;
	private llq.fw.cm.enums.custcontact.ProductEnum product;
	private java.lang.String receiveAccount;
	private String bankReceiving;
	private String sortname;
	private String receiveName;
	private String keyWord;
	private String productValue;
}
