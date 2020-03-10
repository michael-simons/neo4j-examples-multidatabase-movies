package ac.simons.neo4j.examples.multidatabase_movies;

import reactor.core.publisher.Mono;

import java.util.Locale;

import org.neo4j.springframework.data.core.DatabaseSelection;
import org.neo4j.springframework.data.core.ReactiveDatabaseSelectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

	/*
	 * This bean configures relevant features of Spring security:
	 * We want all requests to be authenticated and we don't use CSRF
	 * protection in this application. We support only basic auth.
	 */
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
			.authorizeExchange()
			.anyExchange().authenticated()
			.and()
			.csrf().disable()
			.httpBasic();
		return http.build();
	}

	/*
	 * This creates two users: Michael that has a role of VIDEO-CITY,
	 * and Gerrit, who is a VIDEOS-ARE-US user.
	 */
	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		UserDetails userA = User.withDefaultPasswordEncoder()
			.username("Michael")
			.password("secret")
			.roles("VIDEO-CITY")
			.build();

		UserDetails userB = User.withDefaultPasswordEncoder()
			.username("Gerrit")
			.password("secret")
			.roles("VIDEOS-ARE-US")
			.build();

		return new MapReactiveUserDetailsService(userA, userB);
	}

	/*
	 * This bean is part of SDN/RX. A DatabaseSelectionProvider can be used
	 * to determine the database to use. Here, we choose the reactive security context.
	 */
	@Bean
	ReactiveDatabaseSelectionProvider reactiveDatabaseSelectionProvider() {

		return () -> ReactiveSecurityContextHolder.getContext()
			.map(SecurityContext::getAuthentication)
			.filter(Authentication::isAuthenticated)
			.map(Authentication::getPrincipal)
			.map(User.class::cast)
			.flatMap(u -> Mono.justOrEmpty(u.getAuthorities().stream()
				.map(r -> r.getAuthority().replace("ROLE_", "").toLowerCase(Locale.ENGLISH))
				.map(DatabaseSelection::byName)
				.findFirst()))
			.switchIfEmpty(Mono.just(DatabaseSelection.undecided()));
	}
}
