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
public class UserSearchRequest {
	private java.lang.Long id;
	private java.lang.String branchCode;
	private java.lang.String departmentCode;
	private java.lang.String departmentId;
	private java.lang.String departmentName;
	private java.lang.String email;
	private java.lang.String fullname;
	private java.lang.String password;
	private java.lang.String positionCode;
	private java.lang.String positionId;
	private java.lang.String positionName;
	private llq.fw.cm.enums.IBStatusEnum status;
	private java.lang.String username;
	private java.util.Set refreshtokens;
	private String rolesGroup;
}
