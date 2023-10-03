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
public class RolesGroupFunSearchRequest {
	private java.lang.Long id;
	private String function;
	private String rolesGroup;
	private java.util.Set rolesGroupSubs;
}
