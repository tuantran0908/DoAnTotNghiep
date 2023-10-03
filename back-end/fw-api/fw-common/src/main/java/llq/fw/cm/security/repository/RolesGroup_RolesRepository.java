package llq.fw.cm.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import llq.fw.cm.models.RolesGroup_Roles;

public interface RolesGroup_RolesRepository extends JpaRepository<RolesGroup_Roles, Long>, JpaSpecificationExecutor<RolesGroup_Roles>{

}
