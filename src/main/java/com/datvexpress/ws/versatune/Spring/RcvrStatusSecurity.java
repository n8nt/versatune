package com.datvexpress.ws.versatune.Spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class RcvrStatusSecurity extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .requiresChannel()
//                .anyRequest()
//                .requiresSecure();
/*
        http
                .requiresChannel()
                .anyRequest()
                .requiresSecure();

 */

        //===================================================================

        http
                .authorizeRequests()
                .antMatchers("/lp**","/webjars/**")
                .anonymous()
                .antMatchers("/checkMe","v1/**", "v2/**","v3/**","v4/**","/h2-console/**")
                .permitAll()
                .and()
                .csrf().disable()
                .httpBasic();




        //===================================================================



    }
}
