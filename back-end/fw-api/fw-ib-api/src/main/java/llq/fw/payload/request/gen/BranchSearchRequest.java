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
public class BranchSearchRequest {
	private java.lang.String code;
	private java.lang.String address;
	private java.lang.String fax;
	private java.lang.String location;
	private java.lang.String name;
	private String parentCode;
	private llq.fw.cm.enums.IBStatusEnum status;
	private java.lang.String tel;
	private llq.fw.cm.enums.branch.TypeEnum type;
	private String province;
}
