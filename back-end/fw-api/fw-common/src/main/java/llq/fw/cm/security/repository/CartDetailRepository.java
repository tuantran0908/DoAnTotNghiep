package llq.fw.cm.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import llq.fw.cm.models.CartDetail;
import llq.fw.cm.models.User;

public interface CartDetailRepository  extends JpaRepository<CartDetail, Long>, JpaSpecificationExecutor<CartDetail>{
	long countByUser(User user);
}
