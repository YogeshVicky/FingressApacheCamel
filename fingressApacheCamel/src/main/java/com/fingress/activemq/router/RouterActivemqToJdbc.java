package com.fingress.activemq.router;

import javax.sql.DataSource;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.fingress.activemq.transformer.TransformerActivemqToJdbc;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class RouterActivemqToJdbc extends RouteBuilder {
	static Log log = LogFactory.getLog(RouterActivemqToJdbc.class);

	@Override
	public void configure() {
		String query = "insert into employee (EmployeeId,EmployeeName,EmployeeSalary,Date_Of_Joining) values "
				+ "('${body[EmployeeId]}','${body[EmployeeName]}','${body[EmployeeSalary]}','${body[Date_Of_Joining]}')";
		try {
			HikariConfig config = new HikariConfig();
			config.setDriverClassName("com.mysql.cj.jdbc.Driver");
			config.setUsername("root");
			config.setPassword("root");
			config.setJdbcUrl("jdbc:mysql://localhost:3307/employee");
			DataSource dataSource = new HikariDataSource(config);
			bindToRegistry("mydataSource", dataSource);
			
			from("activemq:queue:Activemq1").process(new TransformerActivemqToJdbc()).split(body())
					.setBody(simple(query)).to("jdbc:mydataSource").log("Sucess inserted from Activemq to Database")
					.end();

		} catch (Exception e) {
			log.error("Invalid request " + e);
			throw new RuntimeException();
		}
	}
}
