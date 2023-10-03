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
public class ProductSearchRequest {
	private java.lang.Long id;
	private java.lang.String code;
	private java.lang.String custDescription;
	private java.lang.String depositsTurm;
	private java.lang.String depositsType;
	private java.lang.String description;
	private java.lang.String document;
	private java.lang.String name;
	private java.lang.String promotions;
	private java.lang.String purpose;
	private java.lang.String rate;
	private llq.fw.cm.enums.IBStatusEnum status;
	private java.lang.String terms;
	private java.lang.String utilities;
	private String productGroup;
	private java.util.Set transFees;
}
