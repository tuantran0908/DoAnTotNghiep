package llq.fw.payload.response;

import java.io.Serializable;
import java.sql.Date;

import llq.fw.cm.enums.IBStatusEnum;
import llq.fw.cm.models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class GiaoTrinhRepsonse extends AuditableResponse implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String author;

	private String description;

	private Float price;
	private Integer sales;
	private IBStatusEnum isNew;
	private Integer quantity;
	private Integer quantityRemaining;
	private Integer quantitySell;
	private Float width;
	private Float height;
	private Float length;
	private Float weight;
	private Double avgStar;
	private byte[] image;
	private Date publicDate;

	private IBStatusEnum status;
	
	private Category category;

}
