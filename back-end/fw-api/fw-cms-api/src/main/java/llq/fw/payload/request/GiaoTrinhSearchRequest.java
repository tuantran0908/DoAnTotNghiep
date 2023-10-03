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
public class GiaoTrinhSearchRequest {
	private Long id;
	private String name;
	private String author;
	private IBStatusEnum status;
	private String description;
	private String publicDateFrom;
	private String publicDateTo;
	private String categoryIds;
	private Boolean saleProduct;
	private Float priceFrom;
	private Float priceTo;
	private Integer star;
	private IBStatusEnum isNew;
	private Boolean bestSeller;
	private Boolean favoriteProduct;
}
