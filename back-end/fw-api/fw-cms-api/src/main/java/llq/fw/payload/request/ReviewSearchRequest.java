package llq.fw.payload.request;

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
public class ReviewSearchRequest {
	private Long id;
	private Integer star;
	private String message;
	private Long userId;
	private Long giaoTrinhId;
}
