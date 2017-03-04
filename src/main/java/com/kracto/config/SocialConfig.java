package com.kracto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource({ "classpath:social.properties" })
public class SocialConfig {
	
	@Autowired
	private Environment env;
	
	@Bean(name = "addThisId")
	public String getAddThisId(){
		return env.getProperty("addthis_id");
	}
	
	@Bean(name = "email")
	public String getEmail(){
		return env.getProperty("email");
	}
	
	@Bean(name = "facebook")
	public String getFacebook(){
		return env.getProperty("facebook");
	}
	
	@Bean(name = "google")
	public String getGoogle(){
		return env.getProperty("google");
	}
	
	@Bean(name = "twitter")
	public String getTwitter(){
		return env.getProperty("twitter");
	}
}
