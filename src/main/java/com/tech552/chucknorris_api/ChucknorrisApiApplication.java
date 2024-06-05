package com.tech552.chucknorris_api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ChucknorrisApiApplication {
	private static final Logger log = LoggerFactory.getLogger(ChucknorrisApiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ChucknorrisApiApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
			Runnable fetchJokeTask = () -> {
					Joke joke = restTemplate.getForObject("https://api.chucknorris.io/jokes/random", Joke.class);
					System.out.println(joke.toString());
			};
			scheduler.scheduleAtFixedRate(fetchJokeTask, 0, 5, TimeUnit.SECONDS);
		};
	}

}
