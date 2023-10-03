package llq.fw.payload.response;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import llq.fw.cm.dto.BlobDeserializer;
import llq.fw.cm.dto.BlobSerializer;
import llq.fw.cm.enums.GenderEnum;
import llq.fw.cm.enums.IBStatusEnum;
import llq.fw.cm.models.Bill;
import llq.fw.cm.models.CartDetail;
import llq.fw.cm.models.Refreshtoken;
import llq.fw.cm.models.Review;
import llq.fw.cm.models.RolesGroup;
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

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class UserResponse extends AuditableResponse implements Serializable {
	private Long id;

	private String email;

	private String fullname;

	private String username;

	private String phoneNumber;
	
	private Date birthday;

	private GenderEnum gender;

	private IBStatusEnum status;
	
	private byte[] avatar;
	
	private RolesGroup rolesGroup;

}