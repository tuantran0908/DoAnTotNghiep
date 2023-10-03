package llq.fw.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import llq.fw.cm.models.User;
import llq.fw.cm.security.services.UserDetailsImpl;

public class AuditorAwareImpl implements AuditorAware<User> {

	@Override
	public Optional<User> getCurrentAuditor() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userRepository.findByUsername(username);

		return Optional.of(SecurityContextHolder.getContext().getAuthentication()).map(e -> {
			if (!(e instanceof AnonymousAuthenticationToken)) {
				UserDetailsImpl userDetails = (UserDetailsImpl) e.getPrincipal();

				return User.builder().id(userDetails.getId()).build();
			}
			return null;
		});
	}

}
