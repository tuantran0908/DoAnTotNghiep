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
public class RolesGroupSearchRequest {
	private java.lang.Long id;
	private java.lang.String departmentCode;
	private java.lang.String departmentId;
	private java.lang.String departmentName;
	private java.lang.String description;
	private java.lang.String name;
	private java.lang.String positionCode;
	private java.lang.String positionId;
	private java.lang.String positionName;
	private llq.fw.cm.enums.IBStatusEnum status;
	private llq.fw.cm.enums.rolesgroup.TypeEnum type;
	private java.util.Set rolesGroupFuns;
	private java.util.Set users;
}
