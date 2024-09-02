package com.artworkmanagement.Utility;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/**").permitAll()
				.antMatchers(HttpMethod.POST, "/api/artists/**", "/api/artworks/**").hasAnyRole("MANAGER", "BUYER")
				.antMatchers(HttpMethod.PATCH, "/api/artworks/{id}/price").hasAnyRole("MANAGER", "BUYER")
				.antMatchers(HttpMethod.PATCH, "/api/artists", "/api/artworks").authenticated()
				.antMatchers(HttpMethod.DELETE, "/api/artists/**", "/api/artworks/**").hasAnyRole("MANAGER", "BUYER")
				.antMatchers("/api/employees/**").hasRole("MANAGER").anyRequest().authenticated().and().httpBasic()
				.and().csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("manager").password("{noop}password").roles("MANAGER").and()
				.withUser("buyer").password("{noop}password").roles("BUYER").and().withUser("staff")
				.password("{noop}password").roles("STAFF");
	}
}
