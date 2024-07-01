package com.fingress.activemq.router;

import javax.sql.DataSource;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.fingress.activemq.transformer.TransformerJdbcToActivemq;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class RouterJdbcToActivemq extends RouteBuilder {
	static Log log = LogFactory.getLog(RouterJdbcToActivemq.class);

	@Override
	public void configure() {
		try {
			String query = "select*from employee";
			HikariConfig config = new HikariConfig();
			config.setDriverClassName("com.mysql.cj.jdbc.Driver");
			config.setUsername("root");
			config.setPassword("root");
			config.setJdbcUrl("jdbc:mysql://localhost:3307/employee");
			DataSource dataSource = new HikariDataSource(config);
			bindToRegistry("mydataSource", dataSource);

			from("timer:sampleTimer?repeatCount=1").setBody(constant(query)).to("jdbc:dataSource")
					.process(new TransformerJdbcToActivemq()).to("activemq:queue:Activemq1")
					.log("Sucess inserted into Database to Activemq").end();
		} catch (Exception e) {
			log.error("Invalid Request" + e);
			throw new RuntimeException();
		}
	}

}
