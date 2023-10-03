package llq.fw.payload.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ForgotPasswordRequest {
	@NotBlank
	private String username;
	
	private String email;
	
	private String tel;
	
	private String cif;
	
	@NotBlank
	private String capcha;
}
