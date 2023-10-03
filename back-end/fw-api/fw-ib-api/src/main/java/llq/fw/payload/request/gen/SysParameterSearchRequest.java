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
public class SysParameterSearchRequest {
	private java.lang.String code;
	private java.lang.String name;
	private java.lang.String type;
	private java.lang.String value;
}
