package vasin.security25.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() {
//       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//       String username = userDetails.getUsername();
        return Optional.of(System.getProperty("user.name"));
    }
}
