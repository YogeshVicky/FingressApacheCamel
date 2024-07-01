package com.fingress.sftp.router;

import javax.sql.DataSource;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fingress.sftp.transformer.TransformerJdbcToSftp;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class RouterJdbcToSftp extends RouteBuilder {
	Log log = LogFactory.getLog(RouterJdbcToSftp.class);

	@Override
	public void configure() {
		try {
			String query2 = "select*from employee";
			HikariConfig config = new HikariConfig();
			config.setDriverClassName("com.mysql.cj.jdbc.Driver");
			config.setUsername("root");
			config.setPassword("root");
			config.setJdbcUrl("jdbc:mysql://localhost:3307/employee");
			DataSource dataSource = new HikariDataSource(config);
			bindToRegistry("mydataSource", dataSource);
			from("timer:run?repeatCount=1").setBody(constant(query2)).to("jdbc:dataSource")
					.process(new TransformerJdbcToSftp())
					.to("sftp://195.154.173.28/sftp_test/outbox?fileName=employees&username=demouser1&password=fujitsu123$%^&knownHostsFile=C:/Users/Admin/.ssh/known_hosts")
					.log("Sucess").end();
		} catch (Exception e) {
			log.error("Invalid request" + e);
			throw new RuntimeException();
		}
	}

}
