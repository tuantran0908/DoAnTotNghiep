package llq.fw.cm.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import llq.fw.cm.models.Bill;

public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill>{
	@Query("SELECT SUM(e.total) FROM Bill e")
	public Long sumTotal();
}
