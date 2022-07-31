package ca.dal.marketplace.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/user/register").permitAll()
                .antMatchers("/api/user/verify").permitAll()
                .antMatchers("/api/user/**").permitAll()
                .antMatchers("/api/users/**").permitAll()
                .antMatchers("/api/posts/**").permitAll()
                .antMatchers("/api/post/**").permitAll()
                .antMatchers("/api/search/**").permitAll()
                .antMatchers("/api/category/**").permitAll()
                .antMatchers("/api/addPost").permitAll()
                .antMatchers("/api/updatePost").permitAll()
                .antMatchers("/api/post-category/**").permitAll()
                .antMatchers("/api/user-posts/**").permitAll()
                .antMatchers("/api/buyer/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
}
