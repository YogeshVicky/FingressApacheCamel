package com.fingress.yamlconfiguration;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@org.springframework.context.annotation.Configuration
public class YamlConfiguration {

	@Bean
	public HikariDataSource myDataSource() {

		HikariConfig config = new HikariConfig();
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		config.setJdbcUrl("jdbc:mysql://localhost:3307/employee");
		config.setUsername("root");
		config.setPassword("root");
		return new HikariDataSource(config);
	}

	@Bean
	public static CamelContextConfiguration contextConfiguration(HikariDataSource myDataSource) {
		return new CamelContextConfiguration() {

			@Override
			public void beforeApplicationStart(CamelContext context) {
				context.getRegistry().bind("myDataSource", myDataSource);
			}

			@Override
			public void afterApplicationStart(CamelContext camelContext) {

			}
		};
	}

}
