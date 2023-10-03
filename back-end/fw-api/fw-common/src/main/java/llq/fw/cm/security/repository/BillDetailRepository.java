package llq.fw.cm.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import llq.fw.cm.models.BillDetail;

public interface BillDetailRepository  extends JpaRepository<BillDetail, Long>, JpaSpecificationExecutor<BillDetail>{

}
