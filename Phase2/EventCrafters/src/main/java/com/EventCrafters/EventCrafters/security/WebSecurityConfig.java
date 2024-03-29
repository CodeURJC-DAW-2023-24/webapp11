package com.EventCrafters.EventCrafters.security;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	RepositoryUserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10, new SecureRandom());
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
    
    @Override
    protected void configure(HttpSecurity https) throws Exception {
    	
    	// Public pages
        https.authorizeRequests().antMatchers("/").permitAll();
        https.authorizeRequests().antMatchers("/login").permitAll();
        https.authorizeRequests().antMatchers("/register").permitAll();
        https.authorizeRequests().antMatchers("/loginerror").permitAll();
        https.authorizeRequests().antMatchers("/event/*").permitAll(); //Placed here for tests, will be moved to private pages soon
        //https.authorizeRequests().antMatchers("/moreEvents").permitAll();
        https.authorizeRequests().antMatchers("/otherEvents").permitAll();
        https.authorizeRequests().antMatchers("/error").permitAll();
        https.authorizeRequests().antMatchers("/ticket").permitAll();
        https.authorizeRequests().antMatchers("/recoverPassword/*").permitAll();
        https.authorizeRequests().antMatchers("/recoverPassword/*/randomToken").permitAll();
        https.authorizeRequests().antMatchers("/profile/img/*").permitAll();



        // Private pages
        https.authorizeRequests().antMatchers("/delete-account").hasAnyRole("USER");
        https.authorizeRequests().antMatchers("/chart-page").hasAnyRole("ADMIN");
        https.authorizeRequests().antMatchers("/ban").hasAnyRole("ADMIN");
        https.authorizeRequests().antMatchers("/unban").hasAnyRole("ADMIN");
        https.authorizeRequests().antMatchers("/logout").hasAnyRole("USER", "ADMIN");
        https.authorizeRequests().antMatchers("/profile").hasAnyRole("USER","ADMIN");
        https.authorizeRequests().antMatchers("/updateProfile").hasAnyRole("USER");
        https.authorizeRequests().antMatchers("/setProfilePicture").hasAnyRole("USER", "ADMIN");
        //https.authorizeRequests().antMatchers("/deleteAccount").hasAnyRole("USER");
        //https.authorizeRequests().antMatchers("/newEvent").hasAnyRole("USER");
        //https.authorizeRequests().antMatchers("/deleteEvent/*").hasAnyRole("USER");
        //https.authorizeRequests().antMatchers("/newReview").hasAnyRole("USER");
        https.authorizeRequests().antMatchers("/newCategory").hasAnyRole("ADMIN");
        https.authorizeRequests().antMatchers("/editCategory/*").hasAnyRole("ADMIN");
        https.authorizeRequests().antMatchers("/deleteCategory/*").hasAnyRole("ADMIN");

        https.authorizeRequests().antMatchers("/create_event").hasAnyRole("USER");
        https.authorizeRequests().antMatchers("/event/edit/*").hasAnyRole("USER", "ADMIN");
        https.authorizeRequests().antMatchers("/review/event/*").hasAnyRole("USER");
        https.authorizeRequests().antMatchers("/event/register/*").hasAnyRole("USER");


        //This line makes it so that, page not listed above are considered public
        //It's commented so that they give an error, just in case we forgot something
        //https.authorizeRequests().anyRequest().permitAll();

        // Login form
        https.formLogin().loginPage("/login");
        https.formLogin().usernameParameter("username");
        https.formLogin().passwordParameter("password");
        https.formLogin().defaultSuccessUrl("/");
        https.formLogin().failureUrl("/loginerror");

        // Logout
        https.logout().logoutUrl("/logout");
        https.logout().logoutSuccessUrl("/login");

        //Disable CSRF at the moment
        //https.csrf().disable();
    }
}
