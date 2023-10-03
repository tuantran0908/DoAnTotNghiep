package llq.fw.cm.payload.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import llq.fw.cm.models.User;
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
@JsonInclude(Include.NON_NULL)
public class UserResponse {
	private Long id;
	private String branchCode;
	private java.util.Date createdAt;
	private String departmentCode;
	private String departmentId;
	private String departmentName;
	private String email;
	private String fullname;
	private String positionCode;
	private String positionId;
	private String positionName;
	private String status;
	private String username;
}
