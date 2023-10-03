package llq.fw.cm.payload.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class SysErrorDefineResponse {

	private String code;

	private String content;

	private String contentEn;
	private java.util.Date createdAt;

	private String description;

	private String function;

	private String name;
	private UserResponse updatedBy;
	private Date updatedAt;
}
