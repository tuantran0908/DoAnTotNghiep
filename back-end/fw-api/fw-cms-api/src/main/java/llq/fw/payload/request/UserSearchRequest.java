package llq.fw.payload.request;

import llq.fw.cm.enums.IBStatusEnum;
import llq.fw.cm.enums.rolesgroup.TypeEnum;
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
	private Long id;
	private String email;
	private String name;
	private IBStatusEnum status;
	private String rolesGroupIds;
	private String createdAtFrom;
	private String createdAtTo;
}
