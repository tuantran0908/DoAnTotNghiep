package llq.fw.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import llq.fw.cm.models.Cust;
import llq.fw.cm.security.services.CustDetailsImpl;

public class AuditorAwareImpl implements AuditorAware<Cust> {

    @Override
    public Optional<Cust> getCurrentAuditor() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userRepository.findByUsername(username);
		return Optional.of(SecurityContextHolder.getContext().getAuthentication()).map(e -> {
			if (!(e instanceof AnonymousAuthenticationToken)) {
				CustDetailsImpl userDetails = (CustDetailsImpl) e.getPrincipal();

				return Cust.builder().id(userDetails.getId()).build();
			}
			return null;
		});
    }

}
