package com.mock.restapi.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mock")
public class MockRestapiApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(MockRestapiApplication.class, args);
	}

}
