package com.fingress.filereading.router;

import javax.sql.DataSource;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import com.fingress.filereading.transformer.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class RouterJdbcToFile extends RouteBuilder {
	Log log = LogFactory.getLog(RouterJdbcToFile.class);

	@Override
	public void configure()  {
		try {
			String query = "select*from employee";
			HikariConfig config = new HikariConfig();
			config.setDriverClassName("com.mysql.cj.jdbc.Driver");
			config.setUsername("root");
			config.setPassword("root");
			config.setJdbcUrl("jdbc:mysql://localhost:3307/employee");
			DataSource dataSource = new HikariDataSource(config);
			bindToRegistry("mydataSource", dataSource);

			from("timer:run?period=2s").setBody(constant(query)).to("jdbc:dataSource")
					.process(new TransformerJdbcToFile()).to("file:file/Output?fileName=employees").log("Sucess").end();
		} catch (Exception e) {
			log.error("Invalid Request" + e);
			throw new RuntimeException();
		}

	}

}