package com.fingress.sftp.router;

import javax.sql.DataSource; 

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.fingress.sftp.transformer.TransformerJdbcToSftp;
import com.fingress.sftp.transformer.TransformerSftpToJdbc;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class RouterSftpToJdbc extends RouteBuilder {
	Log log = LogFactory.getLog(RouterSftpToJdbc.class);

	@Override
	public void configure() {
		try {
			HikariConfig config = new HikariConfig();
			config.setDriverClassName("com.mysql.cj.jdbc.Driver");
			config.setUsername("root");
			config.setPassword("root");
			config.setJdbcUrl("jdbc:mysql://localhost:3307/employee");

			String query1 = "insert into employee (EmployeeId,EmployeeName,EmployeeSalary,Date_Of_Joining) values "
					+ "('${body[EmployeeId]}','${body[EmployeeName]}','${body[EmployeeSalary]}','${body[Date_Of_Joining]}')";
			String query2 = "select*from employee";
			DataSource dataSource = new HikariDataSource(config);
			bindToRegistry("mydataSource", dataSource);

			from("sftp://demouser1@195.154.173.28:22/sftp_test/inbox?username=demouser1&password=fujitsu123$%^&knownHostsFile=C:/Users/Admin/.ssh/known_hosts")
					.log("${body}").process(new TransformerSftpToJdbc()).split(body()).setBody(simple(query1))
					.to("jdbc:mydataSource").setBody(simple(query2)).to("jdbc:mydataSource")
					.process(new TransformerJdbcToSftp())
					.to("sftp://195.154.173.28/sftp_test/outbox?fileName=employees&username=demouser1&password=fujitsu123$%^&knownHostsFile=C:/Users/Admin/.ssh/known_hosts")
					.log("Sucess !!!inserted into table!!!").end();
		} catch (Exception e) {
			log.error("Invalid Request" + e);
			throw new RuntimeException();
		}
	}

}
