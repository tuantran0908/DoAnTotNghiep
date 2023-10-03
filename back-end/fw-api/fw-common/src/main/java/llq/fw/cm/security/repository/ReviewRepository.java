package llq.fw.cm.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import llq.fw.cm.models.Review;

public interface ReviewRepository  extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review>{
	@Query("SELECT AVG(e.star) FROM Review e WHERE e.giaoTrinh.id = ?1")
	public Double getAvgStar(Long giaoTrinhId);
	
}
