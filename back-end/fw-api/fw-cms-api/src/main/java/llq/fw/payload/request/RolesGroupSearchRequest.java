package llq.fw.payload.request;

import java.util.Set;

import llq.fw.cm.enums.IBStatusEnum;
import llq.fw.cm.enums.rolesgroup.TypeEnum;
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
public class RolesGroupSearchRequest {
	private Long id;
	private String description;
	private String name;
	private IBStatusEnum status;
	private String rolesIds;
}
