package llq.fw.payload.request;

import llq.fw.cm.enums.IBStatusEnum;
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
public class ReportSearchRequest {
	private String dateFrom;
	private String dateTo;
}
