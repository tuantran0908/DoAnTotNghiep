package llq.fw.cm.models;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import llq.fw.cm.dto.BlobDeserializer;
import llq.fw.cm.dto.BlobSerializer;
import llq.fw.cm.enums.GenderEnum;
import llq.fw.cm.enums.IBStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.bytebuddy.implementation.bind.annotation.Default;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Table(name = "GIAOTRINH")
public class GiaoTrinh extends Auditable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String author;

	private String description;

	private Float price;
	private Integer sales;
	
	private IBStatusEnum isNew;
	private Integer quantityRemaining;
	private Integer quantitySell;
	private Float width;
	private Float height;
	private Float length;
	private Float weight;
	
	@Lob()
	@Basic(fetch = FetchType.LAZY)
    @Column(name = "IMAGE")
    @JsonSerialize(using = BlobSerializer.class)
    @JsonDeserialize(using = BlobDeserializer.class)
	private Blob image;

	private Date publicDate;

	private IBStatusEnum status;

	// bi-directional many-to-one association to Refreshtoken
	@OneToMany(mappedBy = "giaoTrinh")
	private Set<BillDetail> billDetails;

	@OneToMany(mappedBy = "giaoTrinh")
	private Set<CartDetail> cartDetails;

	@OneToMany(mappedBy = "giaoTrinh")
	private Set<Review> reviews;

	// bi-directional many-to-one association to RolesGroup
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CATEGORYID")
	private Category category;
}
