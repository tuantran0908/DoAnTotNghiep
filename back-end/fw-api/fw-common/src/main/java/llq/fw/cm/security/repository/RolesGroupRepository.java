package llq.fw.cm.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import llq.fw.cm.models.RolesGroup;

public interface RolesGroupRepository extends JpaRepository<RolesGroup, Long>, JpaSpecificationExecutor<RolesGroup> {

}
