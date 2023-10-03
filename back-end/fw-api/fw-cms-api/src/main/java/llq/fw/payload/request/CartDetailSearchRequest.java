package llq.fw.payload.request;

import llq.fw.cm.enums.IBStatusEnum;
import llq.fw.cm.models.GiaoTrinh;
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
public class CartDetailSearchRequest {
	private Long id;
	private Long userId;
}
