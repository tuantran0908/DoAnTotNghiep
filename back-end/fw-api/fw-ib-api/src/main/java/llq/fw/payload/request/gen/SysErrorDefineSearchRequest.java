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
public class SysErrorDefineSearchRequest {
	private java.lang.String code;
	private java.lang.String content;
	private java.lang.String contentEn;
	private java.lang.String description;
	private java.lang.String function;
	private java.lang.String name;
}
