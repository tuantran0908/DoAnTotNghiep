package llq.fw.security.payload.response;

import java.util.List;

import llq.fw.payload.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse extends BaseResponse {
	private String token;
	private String type = "Bearer";
	private String refreshToken;
	private Long id;
	private String username;
	private String fullname;
	private String email;
	private List<String> roles;

	public JwtResponse(String errorCode,String errorMessage, String accessToken, String refreshToken, Long id, String username,String fullname, String email, List<String> roles) {
		super(errorCode,errorMessage);
		this.token = accessToken;
		this.refreshToken = refreshToken;
		this.id = id;
		this.username = username;
		this.fullname = fullname;
		this.email = email;
		this.roles = roles;
	}
}
