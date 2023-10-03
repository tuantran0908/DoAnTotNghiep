package llq.fw.payload.response;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class AuditableResponse {

	protected UserResponse createdBy;

	protected Date createdAt;

	protected UserResponse updatedBy;

	protected Date updatedAt;

}