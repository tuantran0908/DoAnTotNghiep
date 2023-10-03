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
public class CustomerGuideSearchRequest {
	private java.lang.Long id;
	private java.lang.String filename;
	private java.lang.Long findex;
	private java.lang.String name;
	private llq.fw.cm.enums.IBStatusEnum status;
}
