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
public class RouterFileToJdbc extends RouteBuilder {
	Log log = LogFactory.getLog(RouterFileToJdbc.class);

	@Override
	public void configure() {
		try {
			String query = "insert into employee (EmployeeId,EmployeeName,EmployeeSalary,Date_Of_Joining) "
					+ "values ('${body[EmployeeId]}','${body[EmployeeName]}','${body[EmployeeSalary]}','${body[Date_Of_Joining]}')";
			
			HikariConfig config = new HikariConfig();
			config.setDriverClassName("com.mysql.cj.jdbc.Driver");
			config.setUsername("root");
			config.setPassword("root");
			config.setJdbcUrl("jdbc:mysql://localhost:3307/employee");
			DataSource dataSource = new HikariDataSource(config);
			bindToRegistry("mydataSource", dataSource);
			
			from("file:file/Input?noop=true").process(new TransformerFileToJdbc()).split(body()).setBody(simple(query))
					.to("jdbc:mydataSource").log("Sucess").end();
		} catch (Exception e) {
			log.error("Invalid Request" + e);
			throw new RuntimeException();
		}

	}
}