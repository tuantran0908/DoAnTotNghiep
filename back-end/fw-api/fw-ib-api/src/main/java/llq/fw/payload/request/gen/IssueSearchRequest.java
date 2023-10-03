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
public class IssueSearchRequest {
	private java.lang.Long id;
	private java.lang.String answer;
	private java.lang.String iindex;
	private java.lang.String question;
	private llq.fw.cm.enums.IBStatusEnum status;
	private String issueCategory;
}
