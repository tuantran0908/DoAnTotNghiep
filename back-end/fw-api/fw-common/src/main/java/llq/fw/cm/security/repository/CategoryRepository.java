package llq.fw.cm.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import llq.fw.cm.enums.IBStatusEnum;
import llq.fw.cm.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category>{
	List<Category> findAllByStatus(IBStatusEnum status);
	
	long countById(Long id);
}
