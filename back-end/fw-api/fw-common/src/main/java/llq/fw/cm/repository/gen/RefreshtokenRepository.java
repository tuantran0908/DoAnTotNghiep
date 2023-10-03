package llq.fw.cm.repository.gen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import llq.fw.cm.models.Refreshtoken;
public interface RefreshtokenRepository  extends JpaRepository<Refreshtoken, java.lang.Long>, JpaSpecificationExecutor<Refreshtoken> {
}
