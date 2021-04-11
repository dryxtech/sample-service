package com.dryxtech.software.sample.access;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SampleRequestFilter requestFilter;
    private final SampleAuthenticationEntryPoint authenticationEntryPoint;

    public WebSecurityConfig(SampleRequestFilter requestFilter,
                             SampleAuthenticationEntryPoint authenticationEntryPoint) {
        this.requestFilter = requestFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityContextHolderAwareRequestFilter securityContextHolderAwareRequestFilter() {
        return new SecurityContextHolderAwareRequestFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable().authorizeRequests().antMatchers("/api/system").permitAll()
                .and().authorizeRequests().antMatchers("/api/data").fullyAuthenticated()
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.csrf().disable().antMatcher("/api/data")
                .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

