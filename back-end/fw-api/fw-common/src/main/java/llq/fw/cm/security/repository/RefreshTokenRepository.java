package llq.fw.cm.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import llq.fw.cm.models.Refreshtoken;
import llq.fw.cm.models.User;
@Repository
public interface RefreshTokenRepository extends JpaRepository<Refreshtoken, String> {
  Optional<Refreshtoken> findByToken(String token);

  @Modifying
  int deleteByUser(User user);
}
