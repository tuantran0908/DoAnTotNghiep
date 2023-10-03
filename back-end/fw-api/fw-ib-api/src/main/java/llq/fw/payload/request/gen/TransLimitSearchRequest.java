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
public class TransLimitSearchRequest {
	private java.lang.Long id;
	private java.lang.String code;
	private llq.fw.cm.enums.IBStatusEnum status;
	private llq.fw.cm.models.CustType custTypeId;
	private String package1;
	private java.util.Set transLimitDetail;
}
