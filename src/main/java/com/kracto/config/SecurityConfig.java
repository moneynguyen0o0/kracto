package com.kracto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.kracto.data.Role;
import com.kracto.web.constant.URL;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// Retrieve password of current user from spring-security
		auth.eraseCredentials(false);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	// http://stackoverflow.com/questions/33603156/spring-security-multiple-http-config-not-working
	@Configuration
	@Order(1)
	public static class AdminWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher(URL.ADMIN + "/**").authorizeRequests()

					.antMatchers(URL.ADMIN_LOGIN, URL.ADMIN_LOGOUT).permitAll()
					.antMatchers(URL.ADMIN_ACCOUNTS, URL.ADMIN_CATEGORIES, URL.ADMIN_SETTING + URL.MENU)
					.hasAnyRole(Role.SUPERADMIN).anyRequest().hasAnyRole(Role.SUPERADMIN, Role.ADMIN)

					.and().formLogin().loginProcessingUrl(URL.ADMIN_LOGIN).loginPage(URL.ADMIN_LOGIN)
					.failureUrl(URL.ADMIN_LOGIN + "?error=invalid").usernameParameter("username").passwordParameter("password")
					.and().logout().logoutRequestMatcher(new AntPathRequestMatcher(URL.ADMIN_LOGOUT)).logoutSuccessUrl(URL.ADMIN_LOGIN).and()
					.exceptionHandling().accessDeniedPage(URL.ERROR_403);

			// Enable utf-8
			enableCharacterEnCoding(http);
		}
	}

	@Configuration
	public static class UserWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()

					.antMatchers(URL.ACCOUNT_PROFILE, URL.ACCOUNT_CHANGE_PASSWORD).hasAnyRole(Role.MEMBER)

					.and().formLogin().loginProcessingUrl(URL.SIGNIN).loginPage(URL.SIGNIN)
					.failureUrl(URL.SIGNIN + "?error=invalid").usernameParameter("username").passwordParameter("password").and()
					.logout().logoutRequestMatcher(new AntPathRequestMatcher(URL.LOGOUT)).logoutSuccessUrl(URL.WELCOME).and().exceptionHandling()
					.accessDeniedPage(URL.ERROR_403).and().rememberMe().key("uniqueAndSecret").tokenValiditySeconds(1209600);

			// Enable utf-8
			enableCharacterEnCoding(http);
		}
	}

	private static void enableCharacterEnCoding(HttpSecurity http) {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter, CsrfFilter.class);
	}

}