package llq.fw.cm.models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonInclude;

import llq.fw.cm.common.Gen;
import llq.fw.cm.enums.IBStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the ROLES_GROUP database table.
 * 
 */
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Gen(name = "")
@Table(name = "ROLESGROUP")
public class RolesGroup extends Auditable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	private String name;

	private IBStatusEnum status;

	// bi-directional many-to-one association to User
	@OneToMany(mappedBy = "rolesGroup")
	private Set<User> users;

	@OneToMany(mappedBy = "rolesGroup",  fetch = FetchType.EAGER,cascade = { CascadeType.ALL },orphanRemoval = true)
	private Set<RolesGroup_Roles> rolesGroup_Roles;
}