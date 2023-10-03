package llq.fw.security.security.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import llq.fw.cm.models.Refreshtoken;
import llq.fw.cm.models.RefreshtokenIb;
import llq.fw.cm.security.repository.CustRepository;
import llq.fw.cm.security.repository.RefreshTokenIbRepository;
import llq.fw.cm.security.repository.RefreshTokenRepository;
import llq.fw.cm.security.repository.UserRepository;
import llq.fw.security.exception.TokenRefreshException;

@Service
public class RefreshTokenIbService {
  @Value("${bezkoder.app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  @Autowired
  private RefreshTokenIbRepository refreshTokenIbRepository;

  @Autowired
  private CustRepository custRepository;

  public Optional<RefreshtokenIb> findByToken(String token) {
    return refreshTokenIbRepository.findByToken(token);
  }

  public RefreshtokenIb createRefreshToken(Long userId) {
    RefreshtokenIb refreshToken = new RefreshtokenIb();

    refreshToken.setCust(custRepository.findById(userId).get());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenIbRepository.save(refreshToken);
    return refreshToken;
  }
  public RefreshtokenIb verifyExpiration(RefreshtokenIb token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
    	refreshTokenIbRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public int deleteByCustId(Long userId) {
    return refreshTokenIbRepository.deleteByCust(custRepository.findById(userId).get());
  }
}
