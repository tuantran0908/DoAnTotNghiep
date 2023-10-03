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

/**
 * The persistent class for the USERS database table.
 * 
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)

@Table(name = "USERS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id",scope = User.class)
public class User extends Auditable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String fullname;

	private String username;

	@JsonIgnore
	private String password;

	@Column(name = "PHONENUMBER")
	private String phoneNumber;

	@Column(name = "BIRTHDAY")
	private Date birthday;

	@Column(name = "GENDER")
	private GenderEnum gender;

	private IBStatusEnum status;
	
	@Lob()
	@Basic(fetch = FetchType.LAZY)
    @Column(name = "AVATAR")
    @JsonSerialize(using = BlobSerializer.class)
    @JsonDeserialize(using = BlobDeserializer.class)
	private Blob avatar;

	// bi-directional many-to-one association to Refreshtoken
	@OneToMany(mappedBy = "user")
	private Set<Refreshtoken> refreshtokens;
	
	@OneToMany(mappedBy = "user")
	private Set<Bill> bills;
	
	@OneToMany(mappedBy = "user")
	private Set<Review> reviews;
	
	@OneToMany(mappedBy = "user")
	private Set<CartDetail> cartDetail;

	// bi-directional many-to-one association to RolesGroup
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLESGROUPID")
	private RolesGroup rolesGroup;

}