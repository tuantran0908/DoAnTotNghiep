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
public class ProductTypeSearchRequest {
	private java.lang.Long id;
	private java.lang.String code;
	private java.lang.String description;
	private java.lang.String name;
	private java.lang.String nameEn;
	private llq.fw.cm.enums.IBStatusEnum status;
	private java.util.Set productGroups;
}
