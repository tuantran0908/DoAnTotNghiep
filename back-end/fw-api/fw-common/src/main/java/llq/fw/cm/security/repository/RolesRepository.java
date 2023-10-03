package llq.fw.cm.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import llq.fw.cm.models.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long>, JpaSpecificationExecutor<Roles>{

}
