package com.example.bloodbank.project.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig 
{
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) 
    {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery("SELECT username, password, enabled FROM user WHERE username = ?");
        manager.setAuthoritiesByUsernameQuery("SELECT u.username, a.authority FROM authority a " +
                "INNER JOIN user u ON a.user_id = u.id " +
                "WHERE u.username = ?");
        return manager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception 
    {
        http.authorizeHttpRequests(configurer ->
                configurer
//                		Recipient Management
                        .requestMatchers(HttpMethod.POST, "/recipient/add").permitAll()  
                        .requestMatchers(HttpMethod.GET, "/recipient/all").hasAnyAuthority("ROLE_ADMIN")  
                        .requestMatchers(HttpMethod.DELETE, "/recipient/delete/{id}").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/recipient/{id}").hasAuthority("ROLE_RECIPIENT")
                        .requestMatchers(HttpMethod.PUT, "/recipient/update/{id}").hasAuthority("ROLE_ADMIN")


//                      Blood Inventory Management
                        .requestMatchers(HttpMethod.GET, "/bloodstock/get/all").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECIPIENT")  
                        .requestMatchers(HttpMethod.GET, "/bloodstock/get/{bloodType}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/bloodstock/update").hasAuthority("ROLE_ADMIN") 
                        
//                       Blood Request Processing
                        .requestMatchers(HttpMethod.GET, "/api/blood-requests/pending").hasRole("ADMIN") 
                        .requestMatchers(HttpMethod.POST, "/api/blood-requests/create").hasRole("RECIPIENT") 
                        .requestMatchers(HttpMethod.PUT, "/api/blood-requests/approve/{requestId}").hasRole("ADMIN") 
                        .requestMatchers(HttpMethod.PUT, "/api/blood-requests/reject/{requestId}").hasRole("ADMIN") 
      
                        .anyRequest().authenticated()
        );

        http.httpBasic(Customizer.withDefaults()); 
        http.csrf(csrf -> csrf.disable()); 

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() 
    {
        return new BCryptPasswordEncoder();
    }
}

//if error like this Web server failed to start. Port 8080 was already in use.
//lsof -i :8080

//output like this 
//java  12345 user  123u  IPv4  0t0  TCP *:8080 (LISTEN)

//kill -9 12345




