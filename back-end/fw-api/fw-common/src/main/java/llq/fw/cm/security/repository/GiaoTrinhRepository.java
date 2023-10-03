package llq.fw.cm.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import llq.fw.cm.models.Category;
import llq.fw.cm.models.GiaoTrinh;

public interface GiaoTrinhRepository extends JpaRepository<GiaoTrinh, Long>, JpaSpecificationExecutor<GiaoTrinh>{
	long countByCategory(Category category);
}
